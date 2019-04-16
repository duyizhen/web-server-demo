package com.husky.server.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: duyizhen
 * @Date: 2019-04-16 19:02
 * @Version 1.0
 */
public class HttpServer1 {

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer1 http = new HttpServer1();
        http.await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8888;
        try{
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        while(!shutdown){
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                Request request = new Request(inputStream);
                request.parse();

                Response response = new Response(outputStream);
                response.setRequest(request);
                if(request.getUri().startsWith("/servlet/")){
                    ServletProcessor1 servletProcessor1 = new ServletProcessor1();
                    servletProcessor1.process(request, response);
                }
                else{
                    StaticResourceProcessor staticResourceProcessor = new StaticResourceProcessor();
                    staticResourceProcessor.process(request, response);
                }
                socket.close();
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }
    }
}
