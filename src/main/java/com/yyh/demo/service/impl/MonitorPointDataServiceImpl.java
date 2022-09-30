package com.yyh.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyh.demo.commons.Vars;
import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.tdengine.MonitorPointDataMapper;
import com.yyh.demo.service.IMonitorPointDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyh.demo.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 喻云虎
 * @since 2022-08-09
 */
@Service
public class MonitorPointDataServiceImpl extends ServiceImpl<MonitorPointDataMapper, MonitorPointData> implements IMonitorPointDataService {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormatm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat dateFormath = new SimpleDateFormat("yyyy-MM-dd HH");
    @Autowired
    MonitorPointDataMapper monitorPointDataMapper;

    @Override
    public Boolean saveBatch(List<MonitorPointData> monitorPointData) {
        int res = monitorPointDataMapper.insertBatch(monitorPointData);
        return !StringUtils.isEmpty(Integer.toString(res));
    }
    @Override
    public Boolean saveBatch2(List<MonitorPointData> monitorPointData) {
        int res = monitorPointDataMapper.insertBatch2(monitorPointData);
        return !StringUtils.isEmpty(Integer.toString(res));
    }

    @Override
    public String selectByCollectedTime(SearchConditionDto conditionDto) {
        String tagName = conditionDto.getTagName();  //测点id  用于普通表查询
        try {
            conditionDto.setStartTimeStemp(Vars.dateFormat.parse(conditionDto.getStartTime()).getTime());
            conditionDto.setEndTimeStemp(Vars.dateFormat.parse(conditionDto.getEndTime()).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // 两种查询方式：
        // 优先级：普通表查询 > 超级表
        // 1. 普通表查询-：
        // 不能设置tag查询条件，否则报错TDengine ERROR (80000200): invalid operation: filter on tag not supported for normal table
        if (StringUtils.isNotEmpty(tagName))
            conditionDto.setTName("`" + tagName + "`");
        else {
            // 2.超级表查询：
            // 如果不设置tag条件，则扫描超级表下所有子表，
            // 如果设置了tag条件，先根据tag值过滤子表，再从过滤后的子表中查询
            conditionDto.setStName("st_data");
            if (StringUtils.isNotEmpty(conditionDto.getCode2()))
                conditionDto.setCode9(conditionDto.getCode2());  // 设置其他tag查询条件
            if (StringUtils.isNotEmpty(conditionDto.getCode4()))
                conditionDto.setCode9(conditionDto.getCode4());  // 设置其他tag查询条件
            if (StringUtils.isNotEmpty(conditionDto.getCode6()))
                conditionDto.setCode9(conditionDto.getCode6());  // 设置其他tag查询条件
            if (StringUtils.isNotEmpty(conditionDto.getCode9()))
                conditionDto.setCode9(conditionDto.getCode9());  // 设置其他tag查询条件
            if (StringUtils.isNotEmpty(conditionDto.getCode12()))
                conditionDto.setCode9(conditionDto.getCode12());  // 设置其他tag查询条件
        }
        List<MonitorPointData> list = monitorPointDataMapper.selectByConditions(conditionDto);
        return new Result().data(list).success();
    }

    @Override
    public String supplementDataByDate(String startTime, String endTime) {
        long endTimeStamp = 0;
        Date currTime = null;
        try {
            endTimeStamp = dateFormat.parse(endTime).getTime();
            currTime = dateFormat.parse(startTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        while (currTime.getTime() <= endTimeStamp) {
            try {
                System.out.println(dateFormat.format(currTime));
                try {
                    supplementData(Vars.pointInfoList, currTime);
                } catch (ParseException e) {
                    System.out.println("插入"+currTime+"抛出异常");
                    throw new RuntimeException(e);
                }
                Calendar calendar  = Calendar.getInstance();
                calendar.setTime(currTime);
                calendar.add(Calendar.MINUTE, 5);
                currTime = dateFormat.parse(dateFormat.format(calendar.getTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return new Result().message("从 " + startTime + " 到 " + endTime + " 补充完毕").success();
    }

    HashMap<String, Double> pointValues = new HashMap<>();
    public String supplementData(List<MonitorPointInfo> pointInfos, Date currTime) throws ParseException {
        long spS = System.currentTimeMillis();
        String centerTimeStr = dateFormatm.format(currTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormatm.parse(centerTimeStr));
        cal.add(Calendar.MINUTE, -3);
        String leftTime = dateFormatm.format(cal.getTime());
        cal.add(Calendar.MINUTE, 6);
        String rightTime = dateFormatm.format(cal.getTime());
        List<MonitorPointData> monitorPointDataList = new ArrayList<>();
        int count = 0;
        SearchConditionDto searchConditionDto = new SearchConditionDto();
        long t1 = System.currentTimeMillis();
        for (MonitorPointInfo pointInfo : pointInfos) {
            searchConditionDto.setStartTime(leftTime);
            searchConditionDto.setEndTime(rightTime);
            searchConditionDto.setTagName(pointInfo.getTagName());
            List<MonitorPointData> monitorPointDatas = monitorPointDataMapper.selectFirstByConditions(searchConditionDto);
            MonitorPointData monitorPointData = new MonitorPointData();
            if (monitorPointDatas.size() == 0) {  // 没有查到数据需要补充
                Double dataValue = pointValues.get(pointInfo.getTagName());
                if (dataValue == null) {
                    searchConditionDto.setTagName(pointInfo.getTagName());
                    searchConditionDto.setStartTime("2022-03-30 00:00:00");
                    searchConditionDto.setEndTime(leftTime);
                    monitorPointDatas = monitorPointDataMapper.selectLastByConditions(searchConditionDto);
                    if (monitorPointDatas.size() == 0) {
                        searchConditionDto.setEndTime(dateFormat.format(new Date()));
                        monitorPointDatas = monitorPointDataMapper.selectFirstByConditions(searchConditionDto);
                    }
                    if (monitorPointDatas.size() != 0) {
                        monitorPointData = monitorPointDatas.get(0);
                        monitorPointData.setCollectedTime(centerTimeStr);
                        monitorPointData.setTName("`" + pointInfo.getTagName() + "`");
                        monitorPointData.setTagName(pointInfo.getTagName());
                        monitorPointDataList.add(monitorPointData);
                        MonitorPointData data = monitorPointDatas.get(0);
                        pointValues.put(pointInfo.getTagName(), data.getDataValue());
                    }
                }else {
                    monitorPointData.setCollectedTime(centerTimeStr);
                    monitorPointData.setTName("`" + pointInfo.getTagName() + "`");
                    monitorPointData.setTagName(pointInfo.getTagName());
                    monitorPointData.setDataValue(pointValues.get(pointInfo.getTagName()));
                    monitorPointDataList.add(monitorPointData);
                }

            }
            count++;
//            System.out.printf(String.valueOf(count) + " ");
//            if (count >= 20) break;
        }
//        System.out.println(System.currentTimeMillis() - t1);
        if (monitorPointDataList.size() > 0) {
            System.out.println("List all " + monitorPointDataList.size());
            saveBatch2(monitorPointDataList);
            System.out.println("Tdd "+centerTimeStr + " spend " + (System.currentTimeMillis() - spS) + "ms");
        }
        return new Result().message("补充"+centerTimeStr + ": " + monitorPointDataList.size()).success();
    }
}
