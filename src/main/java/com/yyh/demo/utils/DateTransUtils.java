package com.yyh.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransUtils {

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = Long.parseLong(stamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDate(long stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将毫秒转换成时分秒毫秒的格式
     *
     * @param time ==>  60*60*1000
     * @return
     */
    public static String msecToTime(long time) {

        long hours = time / (3600000);
        long minutes = (time % 3600000) / 60000;
        long seconds = (time % 60000) / 1000;
        long msec = (time % 1000);
        return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, msec);
    }

    public static String msecToTime(String time) {
        long temp = Long.parseLong(time);
        long hours = temp / (3600000);
        long minutes = (temp % 3600000) / 60000;
        long seconds = (temp % 60000) / 1000;
        long msec = (temp % 1000);
        return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, msec);
    }

    /**
     * 将字符串切割成年月日
     *
     * @param time ==>  20160923
     * @return
     */
    public static String stringToCalendar(String time) {
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String date = year + "-" + month + "-" + day;
        return date;
    }


    /**
     *
     * 返回当前时间的14位字符串
     * @return
     */
    public static String dateToString(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String dateStr = format.format(date);
        return dateStr;
    }

    public static String dateToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = format.format(date);
        return dateStr;
    }


}
