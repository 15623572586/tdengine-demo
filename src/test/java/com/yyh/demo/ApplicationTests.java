package com.yyh.demo;

import com.yyh.demo.commons.Vars;
import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.mapper.tdengine.MonitorPointDataMapper;
import com.yyh.demo.mapper.tdengine.TableManagementMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@SpringBootTest
class ApplicationTests {

    @Autowired
    private MonitorPointDataMapper monitorPointDataMapper;
    @Autowired
    private TableManagementMapper tableManagementMapper;

    @Test
    public void testSaveBatch() {
        List<MonitorPointData> list = new ArrayList<>();
//        MonitorPointData data = new MonitorPointData();
//        data.setStName("code_5286");
//        data.setTName("p_1514802221856849900");  // 测点ID
//        data.setCollectedTime(dateFormat.format(new Date()));
//        data.setDataValue(Math.random());
//        data.setTagName("tagName1");
//        data.setDepartment("01");  // 厂部
//        data.setProductionLine("0102");
//        data.setRegion("010203");
//        data.setCode9("010203001");
//        list.add(data);
        MonitorPointData data1 = new MonitorPointData();
        data1.setStName("code_5212");
        data1.setTName("p_1514802221856849901");  // 测点ID
        data1.setCollectedTime(Vars.dateFormat.format(new Date()));
        data1.setDataValue(Math.random());
        data1.setTagName("tagName2");
        data1.setDepartment("01");  // 厂部
        data1.setProductionLine("0102");
        data1.setRegion("010203");
        data1.setCode9("010203008");
        list.add(data1);
        tableManagementMapper.createSTable(data1.getStName());
        int insertCount = monitorPointDataMapper.insertBatch(list);
        System.out.println(insertCount);
    }
    @Test
    public void testSelectByCondition() throws ParseException {
        SearchConditionDto conditionDto = new SearchConditionDto();
//        conditionDto.setTName("p_1514802221856849902");
        conditionDto.setStName("code_5212");
        // java.lang.NoSuchMethodError: com.alibaba.fastjson.JSONArray.getTimestamp(I)Ljava/lang/Object;
        // fastjson升级到1.2.80
        conditionDto.setStartTimeStemp(Vars.dateFormat.parse("2022-08-09 16:00:00").getTime());
        conditionDto.setEndTimeStemp(Vars.dateFormat.parse("2022-09-09 18:42:00").getTime());
        conditionDto.setCode9("010203008");
        List<MonitorPointData> list = monitorPointDataMapper.selectByConditions(conditionDto);
        System.out.println(list);
    }

}
