package com.sunrise.netty.studyapi.nio;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 8:15 PM
 */
public class NioTimeClient {
    private static int port = 8888;
    public static void main(String[] args) {
        new Thread(new NioTimeHandler("127.0.0.1",port),"NioTimeClient").start();
    }
}
