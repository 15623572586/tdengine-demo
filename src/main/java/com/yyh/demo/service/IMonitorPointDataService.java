package com.yyh.demo.service;

import com.yyh.demo.dto.SearchConditionDto;
import com.yyh.demo.entity.MonitorPointData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yyh.demo.entity.MonitorPointInfo;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 喻云虎
 * @since 2022-08-09
 */
public interface IMonitorPointDataService extends IService<MonitorPointData> {
    Boolean saveBatch(List<MonitorPointData> monitorPointData);
    Boolean saveBatch2(List<MonitorPointData> monitorPointData);

//    Boolean saveBatch(List<MonitorPointData> monitorPointData);

    String selectByCollectedTime(SearchConditionDto conditionDto);

    String supplementDataByDate(String startTime, String endTime);
}
