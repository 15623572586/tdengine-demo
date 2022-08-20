//package com.yyh.demo.utils;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.sql.*;
//import java.util.*;
//
///**
// * @author 喻云虎
// * @description
// * @date 2021/11/12 22:59
// */
//@Component
//public class DbUtil {
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String dbDriver;
//    @Value("${spring.datasource.url}")
//    private String url;
//    @Value("${spring.datasource.username}")
//    private String username;
//    @Value("${spring.datasource.password}")
//    private String password;
//
//
//    public String insertOneRecord(String tableName, HashMap<String, Object> feilds) {
//        Connection conn = null;
//        Statement stmt = null;
//        Integer insertRes = null;
//        try{
//            // 注册 JDBC 驱动
////            Class.forName("com.mysql.cj.jdbc.Driver");
//            Class.forName(dbDriver);
//
//            // 打开链接
//            conn = DriverManager.getConnection(url, username, password);
////            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/message?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root","123456");
//            if (conn == null) {
//                System.out.println("发起流程向报告分表插入数据时连接数据库失败");
//                return new Result().message("连接数据库失败").fail();
//            }
//            stmt = conn.createStatement();
//            if (stmt == null) {
//                System.out.println("发起流程向报告分表插入数据时Statement实例化失败");
//                return new Result().message("Statement实例化失败").fail();
//            }
//            StringBuilder feildsStr = new StringBuilder();
//            StringBuilder paramsValues = new StringBuilder();
//            int i = 0;
//            for (String key : feilds.keySet()) {
//                if (i > 0) {
//                    feildsStr.append(",");
//                    paramsValues.append(",");
//                }
//                feildsStr.append(key);
//                paramsValues.append(feilds.get(key));
//                i++;
//            }
//            String sql = "INSERT  INTO  "+ tableName + "(" + feildsStr +") VALUES (" + paramsValues + ")";
//            insertRes = stmt.executeUpdate(sql);
//            // 完成后关闭
////            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            // 处理 JDBC 错误
//            se.printStackTrace();
//        }catch(Exception e){
//            // 处理 Class.forName 错误
//            e.printStackTrace();
//        }finally{
//            // 关闭资源
//            try{
//                if(stmt!=null) stmt.close();
//            }catch(SQLException se2){
//            }// 什么都不做
//            try{
//                if(conn!=null) conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//        if (insertRes == null) {
//            return new Result().message("插入失败").fail();
//        }
//        return new Result().message("插入成功").data(insertRes).success();
//    }
//
//    public  static  void main(String[] args) throws SQLException {
//
////        HashMap<String, Object> map = new HashMap<>();
////        map.put("message_id", "1");
////        map.put("id_card", "1");
////        insertOneRecord("messages", map);
//
//        Connection conn=null;
//        PreparedStatement pst =null;
//
//        try {
//            // 1 加载驱动类
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            // 2 通过DriverManager获取connection对象
////            String url="jdbc:mysql://localhost:3306/message?user=root&password=123456&useUnicode=true&characterEncoding=UTF8&serverTimezone=Asia/Shanghai";
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/message?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//                    "root","123456");
//            if (!conn.isClosed()){
//                System.out.println("Succeeded connecting to the Database!");
//            }else{
//                System.out.println("Sorry,failed  connecting to the Database");
//            }
//            String tableName = "messages";
//            String feildsStr="message_id";
//            String params = "?";
//
//            // 3 获取pre对象
//            String sql = "INSERT  INTO  "+ tableName + "(" + feildsStr +") VALUES (" + params + ")";
//            pst = conn.prepareStatement(sql);
//            //为sql参数赋值
//            pst.setString(1, String.valueOf(UUID.randomUUID()));
//            //4  使用prepare对象执行sql语句
//            int count = pst.executeUpdate();
//            System.out.println("影响数据库的条数为："+count);
//            //5 操作result结果集
//        }catch (ClassNotFoundException e){
//            e.printStackTrace();
//        }finally {
//            // 6 关闭连接
//            if (pst != null) {
//                pst.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        }
//    }
//}
