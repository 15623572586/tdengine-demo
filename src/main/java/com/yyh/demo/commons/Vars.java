package com.yyh.demo.commons;

import com.yyh.demo.entity.MonitorPointInfo;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Vars {

    public static HashMap<String, MonitorPointInfo> pointInfos = new HashMap<>();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
