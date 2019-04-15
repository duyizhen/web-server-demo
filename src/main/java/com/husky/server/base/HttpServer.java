package com.husky.server.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: duyizhen
 * @Date: 2019-04-15 17:17
 * @Version 1.0
 */


public class HttpServer {

    // 文件存储的根路径
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    // 关闭指令
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    // 是否关闭连接
    private boolean shutdown = false;

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8888;
        try{
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        // 循环方式, 获取连接
        while(!shutdown){
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try{
                // 阻塞监听
                socket = serverSocket.accept();
                // 从socket获取输入流
                inputStream = socket.getInputStream();
                // 从socket获取输出流
                outputStream = socket.getOutputStream();
                // 创建请求对象
                Request request = new Request(inputStream);
                // 解析http请求
                request.parse();
                System.out.println("uri:" + request.getUri());
                System.out.println("HTTPType:" + request.getHttpType());
                // 创建响应数据
                Response response = new Response(outputStream);
                // 设置请求对象
                response.setRequest(request);
                // 发送请求的数据
                response.senStaticResource();
                // 关闭socket连接
                socket.close();
                // 判断是否关闭服务器
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }
}