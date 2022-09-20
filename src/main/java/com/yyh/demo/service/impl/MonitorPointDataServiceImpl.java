package com.yyh.demo.service.impl;

import com.yyh.demo.commons.Vars;
import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.yyh.demo.entity.MonitorPointInfo;
import com.yyh.demo.mapper.tdengine.MonitorPointDataMapper;
import com.yyh.demo.service.IMonitorPointDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyh.demo.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            conditionDto.setStName("st_lz");
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
}
