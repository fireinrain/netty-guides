package com.sunrise.netty.studyapi.aio;

/**
 * @description: 异步IO
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 10:39 PM
 */
public class AioTimeServer {
    private static int port = 8888;

    public static void main(String[] args) {
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler,"asyncTimeServerHandler@8888").start();
    }
}
