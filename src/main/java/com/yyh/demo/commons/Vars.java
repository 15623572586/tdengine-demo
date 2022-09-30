package com.yyh.demo.commons;

import com.yyh.demo.entity.MonitorPointInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vars {

    public static HashMap<String, MonitorPointInfo> pointInfos = new HashMap<>();

    public static List<MonitorPointInfo> pointInfoList = new ArrayList<>();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String startTime;
    public static String endTime;

}
