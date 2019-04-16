package com.husky.server.simple;

import com.husky.server.base.HttpServer;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

/**
 * @Author: duyizhen
 * @Date: 2019-04-16 19:10
 * @Version 1.0
 */
public class Response implements ServletResponse {

    // 一次读取的最大字节
    private static final int BUFFER_SIZE = 1024;
    // request 类, 从中获取http响应头信息, 通过uri来判断获取的资源
    Request request;
    // 输出流 来自Socket 请求的数据通过输出流传送
    OutputStream outputStream;

    PrintWriter printWriter;

    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(Request request){
        this.request = request;
    }

    public String getResponseHearder(int fileLength){
        String contentLength;
        if(fileLength > 0){
            contentLength = "Content-Length: " + fileLength + "\r\n";
        }else{
            contentLength = "";
        }
        String hearder = "HTTP/1.1 200 ok\r\n" +
                "Content-Type: text/html\r\n" +
                contentLength +
                "\r\n" ;
        return hearder;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;
        try{
            // 从文件服务器位置读取指定文件
            File file = new File(Constants.WEB_ROOT, request.getUri());
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                String responseHeader;
                int ch = fileInputStream.read(bytes, 0, BUFFER_SIZE);
                if(ch < BUFFER_SIZE){
                    // 如果一次读取成功 发送数据长度
                    responseHeader = getResponseHearder(ch);
                }else{
                    // 若是一次读取不成功, 不发送数据长度
                    responseHeader = getResponseHearder(-1);
                }
                // 发送请求头
                outputStream.write(responseHeader.getBytes());
                while(ch != -1){
                    // 发送请求数据
                    outputStream.write(bytes, 0, ch);
                    ch = fileInputStream.read(bytes, 0, BUFFER_SIZE);
                }
            }else{
                System.out.println("File Not Found");
                String errorMessae = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                outputStream.write(errorMessae.getBytes());
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
        }finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
        }

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        printWriter = new PrintWriter(outputStream, true);
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
