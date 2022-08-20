package com.yyh.demo.entity;

import lombok.Data;

@Data
public class MonitorPointInfo {
    private String equipmentCode;
    private String pointId;
    private String tagName;
    private String department;
    private String produtionLine;
    private String region;

    public MonitorPointInfo() {
        this.department = "coldrolling";
    }
}
