package com.sunrise.netty.studyapi.nio;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 5:16 PM
 */
public class NioTimeServer {
    private static int port = 8888;

    public static void main(String[] args) {
        SelectTimeServer selectTimeServer = new SelectTimeServer(port);

        new Thread(selectTimeServer,"selectTimeServer@8888").start();
    }
}
