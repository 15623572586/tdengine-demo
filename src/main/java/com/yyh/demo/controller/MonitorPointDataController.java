package com.yyh.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.errorprone.annotations.Var;
import com.yyh.demo.commons.Vars;
import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.entity.TableInfo;
import com.yyh.demo.mapper.tdengine.MonitorPointDataMapper;
import com.yyh.demo.mapper.tdengine.TableManagementMapper;
import com.yyh.demo.service.IMonitorPointDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 喻云虎
 * @since 2022-08-09
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/demo/monitor-point-data")
public class MonitorPointDataController {
    @Autowired
    private MonitorPointDataMapper monitorPointDataMapper;
    @Autowired
    private TableManagementMapper tableManagementMapper;

    @Autowired
    private IMonitorPointDataService monitorPointDataService;

    @PostMapping("saveBatch")
    public String saveBatch() {
        List<MonitorPointData> monitorPointDataList = new ArrayList<>();
        for (String key : Vars.pointInfos.keySet()) {
            MonitorPointInfo info = Vars.pointInfos.get(key);
            MonitorPointData data = new MonitorPointData();
            data.setStName("st_" + info.getRegion());  // 四码--超级表
            data.setTName("t_" + key);  // 测点ID
            data.setCollectedTime(Vars.dateFormat.format(new Date()));
            data.setDataValue(Math.random());
            data.setTagName(info.getTagName());
            data.setDepartment(info.getDepartment());  // 厂部
            data.setProductionLine(info.getProdutionLine());
            data.setRegion(info.getRegion());
            data.setCode9(info.getEquipmentCode().substring(0,9));
            monitorPointDataList.add(data);
        }
        return monitorPointDataService.saveBatch(monitorPointDataList) ? "ok" : "failed";
    }
    List<String> times = Arrays.asList("2022-06-25 12:00:00", "2022-06-25 12:23:00", "2022-06-25 12:28:00");
    @PostMapping("saveBatch2")
    public String saveBatch2() {
        List<MonitorPointData> monitorPointDataList = new ArrayList<>();
        for (String time: times) {
            int count = 0;
            monitorPointDataList = new ArrayList<>();
            for (MonitorPointInfo pointInfo : Vars.pointInfoList) {
                if (StringUtils.isEmpty(pointInfo.getTagName())) {
                    System.out.println("null" + pointInfo.getTagName());
                    continue;
                }
                MonitorPointData data = new MonitorPointData();
                data.setStName("st_data");  // 超级表
                data.setTName("`" + pointInfo.getTagName() + "`");  // 测点tagname
                try {
                    data.setCollectedTime(Vars.dateFormat.format(Vars.dateFormat.parse(time)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                data.setDataValue(Math.random());
                data.setTagName(pointInfo.getTagName());
                // 插入数据时不设置tag
                data.setCode2(pointInfo.getEquipmentCode().substring(0, 2));  // 厂部
                data.setCode4(pointInfo.getEquipmentCode().substring(0, 4));
                data.setCode6(pointInfo.getEquipmentCode().substring(0, 6));
                data.setCode9(pointInfo.getEquipmentCode().substring(0, 9));
                data.setCode12(pointInfo.getEquipmentCode());
                monitorPointDataList.add(data);
                count++;
                if (count == 20) break;
            }
            try {
                monitorPointDataService.saveBatch2(monitorPointDataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  "ok";
    }
    @GetMapping("getPointDataByCondition")
    public String getPointDataByCondition(@RequestBody SearchConditionDto conditionDto) throws ParseException {
        return monitorPointDataService.selectByCollectedTime(conditionDto);
    }

    @PostMapping("createTableData")
    public String createTableData(@RequestBody List<TableInfo> tableInfos) {
        if (tableInfos.isEmpty()) {
            // 业务场景下，如果tablesInfos为空则返回报错信息
            tableInfos = new ArrayList<>();
            for (MonitorPointInfo pointInfo: Vars.pointInfoList ) {
                if (tableInfos.size()>=20) break;
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName("`"+pointInfo.getTagName()+"`");
                tableInfo.setTagName(pointInfo.getTagName());
                tableInfo.setCode2(pointInfo.getEquipmentCode().substring(0,2));  // 厂部
                tableInfo.setCode4(pointInfo.getEquipmentCode().substring(0,4));
                tableInfo.setCode6(pointInfo.getEquipmentCode().substring(0,6));
                tableInfo.setCode9(pointInfo.getEquipmentCode().substring(0,9));
                tableInfo.setCode12(pointInfo.getEquipmentCode());
                tableInfos.add(tableInfo);
            }
        }
        return String.valueOf(tableManagementMapper.createTableData(tableInfos));
    }
    @PostMapping("supplementData")
    public String supplementData(@RequestBody JSONObject jsonObject) {
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        return monitorPointDataService.supplementDataByDate(startTime, endTime);
    }
    @GetMapping("createSt")
    public String createSt() {
        tableManagementMapper.createSTableData();
        return "ok";
    }
}

