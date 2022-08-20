package com.yyh.demo.dto;

import com.yyh.demo.entity.MonitorPointData;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class SearchConditionDto extends MonitorPointData {
    private String pointId;
    private String startTime;
    private String endTime;
    // 时间
    private Long startTimeStemp;
    private Long endTimeStemp;
}
