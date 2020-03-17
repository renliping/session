package com.example.demo.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

public class HttpClientResult implements Serializable {

    /**
     * 响应状态码
     */
    private int code;


    /**
     * 响应数据
     */
    private String content;
    /**
     * 响应头
     */
    private Map<String,String> headerMap=new HashMap<>();

    HttpClientResult(int code) {
        this.code=code;
    }

    HttpClientResult(int code,String content,Map<String,String> headerMap){
        this.code=code;
        this.content=content;
        this.headerMap = headerMap;
    }
    public JSONObject getResult(){
        return JSONObject.parseObject(content);
    }
}
