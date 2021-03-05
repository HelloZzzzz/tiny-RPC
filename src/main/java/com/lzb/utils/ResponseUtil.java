package com.lzb.utils;

/**
 * @Author : LZB
 * @Date : 2021/3/4
 * @Description :
 */
public final class ResponseUtil {
    private ResponseUtil() {

    }

    public static final int SC_OK = 200;

    public static String getHttpHeader200(String content) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + content.length() + " \n" +
                "\r\n" + content;
    }


}
