package com.husky.server.simple;

import java.io.File;

/**
 * @Author: duyizhen
 * @Date: 2019-04-16 19:31
 * @Version 1.0
 */
public class Constants {
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public static final String WEB_ROOT2 = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes"
            + File.separator + "com" + File.separator + "husky" + File.separator + "server" + File.separator + "simple";
}
