package com.husky.server.simple;

import java.io.IOException;

/**
 * @Author: duyizhen
 * @Date: 2019-04-16 19:17
 * @Version 1.0
 */
public class StaticResourceProcessor {

    public void process(Request request, Response response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
