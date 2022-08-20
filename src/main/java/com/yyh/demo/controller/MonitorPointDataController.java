package com.yyh.demo.controller;


import com.yyh.demo.commons.Vars;
import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.tdengine.MonitorPointDataMapper;
import com.yyh.demo.service.IMonitorPointDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 喻云虎
 * @since 2022-08-09
 */
@RestController
@RequestMapping("/demo/monitor-point-data")
public class MonitorPointDataController {
    @Autowired
    private MonitorPointDataMapper monitorPointDataMapper;

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
    @GetMapping("getPointDataByCondition")
    public String getPointDataByCondition(@RequestBody SearchConditionDto conditionDto) throws ParseException {
        return monitorPointDataService.selectByCollectedTime(conditionDto);
    }
}

