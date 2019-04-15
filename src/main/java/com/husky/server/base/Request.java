package com.husky.server.base;

import java.io.IOException;
import java.io.InputStream;

/**
 * 模拟http请求类
 * @Author: duyizhen
 * @Date: 2019-04-15 17:15
 * @Version 1.0
 */

public class Request {

    // 从socket中获取输入流, 从中获取请求数据
    private InputStream inputStream;

    // 记录请求的uri
    private String uri;

    // http请求方式
    private String httpType;

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }

    // 解析http请求
    public void parse(){
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try{
            i = inputStream.read(buffer);
        }catch (IOException e){
            e.printStackTrace();
            i = -1;
        }
        for(int j = 0; j < i; j++){
            request.append((char)buffer[j]);
        }
        System.out.println("requestString:");
        System.out.println(request.toString());
        uri = parseUri(request.toString());
        httpType = parseHttpType(request.toString());
    }

    /**
     * 解析请求头 利用http标准格式来获取
     * 在http协议中 uri在 第一行, 两个" "(空格) 之间
     * GET /index.html HTTP/1.1
     * @param requestString
     * @return
     */
    private String parseUri(String requestString){
        int index1, index2;
        index1 = requestString.indexOf(" ");
        if(index1 != -1){
            index2 = requestString.indexOf(" ", index1 + 1);
            if(index2 > index1){
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    /**
     * 解析http请求方式 GET POST
     * @param requestString
     * @return
     */
    private String parseHttpType(String requestString){
        int index;
        index = requestString.indexOf(" ");
        if(index != -1){
            return requestString.substring(0, index);
        }
        return null;
    }

    public String getUri(){
        return uri;
    }

    public String getHttpType(){
        return httpType;
    }
}