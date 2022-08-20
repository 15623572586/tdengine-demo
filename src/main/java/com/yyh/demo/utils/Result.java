package com.yyh.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Result {
    public int status;
    public String message;
    public Object data;

    public Result(){
        this.status = 0;
        this.message = "";
        this.data = new Object();
    }
    public Result message(String m ){
        this.message = m;
        return this;
    }
    public Result data(Object data ){
        this.data = data;
        return this;
    }
    public String fail(){
        this.status = 1;
        return Result.toJSON(this);
    }
    public String fail(String msg){
        this.message(msg);
        this.status = 1;
        return Result.toJSON(this);
    }
    public String success(){
        this.status = 0;
        return Result.toJSON(this);
    }

    public static String toJSON(Result as){
        return JSON.toJSONString(as, SerializerFeature.WriteMapNullValue);
    }
}
