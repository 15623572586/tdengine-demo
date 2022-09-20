package com.yyh.demo.dto;

import com.yyh.demo.entity.MonitorPointData;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class SearchConditionDto extends MonitorPointData {
    private String tagName;
    private String code2;
    private String code4;
    private String code6;
    private String code9;
    private String code12;
    private String startTime;
    private String endTime;
    // 时间
    private Long startTimeStemp;
    private Long endTimeStemp;
}
