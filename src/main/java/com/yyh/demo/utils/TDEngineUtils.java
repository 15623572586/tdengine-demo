package com.yyh.demo.utils;

import com.taosdata.jdbc.TSDBDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TDEngineUtils {

    static Connection conn = null;
    public static Connection getConn() throws Exception{
        Class.forName("com.taosdata.jdbc.TSDBDriver");
        String jdbcUrl = "jdbc:TAOS://175.178.8.159:6030/test?user=root&password=ontoweb";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        connProps.setProperty("debugFlag", "135");
        connProps.setProperty("maxSQLLength", "1048576");
        conn = DriverManager.getConnection(jdbcUrl, connProps);
        return conn;
    }

    public static Connection getRestConn() throws Exception{
        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
        String jdbcUrl = "jdbc:TAOS-RS://175.178.8.159:6041/test?user=root&password=ontoweb";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_BATCH_LOAD, "true");
        conn = DriverManager.getConnection(jdbcUrl, connProps);
        return conn;
    }
    public static void closeConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static String[] strings = new String[]{
            "`~1`","`~`" ,"`哈哈`","`!`","`@`","`#`","`%`","`^`","`&`","`*`","`(1)`",
            "`-`","`_`","`+`","`=`","`\\q`","`|`","`}`","`[`","`'`",  // 字符串中如果有有斜杠，需要做特殊处理,转义
            "`1100`","`\"`","`;`","`:`","`.`","`,`","`?`","`>`","`<`","`·`", //双引号也需要转义
            "`￥`","`……`","`（`","` `","`-`","`——`","`【`","`/`","`，`","`《`","`？`"
    };

    public static void main(String[] args) throws SQLException {
        try {
            getConn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Statement statement = conn.createStatement();
        statement.executeUpdate("use test");
        for (String str : strings) {
            try {
                statement.executeUpdate("create table if not exists "+ str +" (ts timestamp, temperature int, humidity float)");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(str);
            }
        }
        closeConn();
    }
}
