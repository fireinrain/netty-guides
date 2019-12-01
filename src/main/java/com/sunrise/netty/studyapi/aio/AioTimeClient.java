package com.sunrise.netty.studyapi.aio;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 11:43 PM
 */
public class AioTimeClient {
    private static int port = 8888;

    public static void main(String[] args) {
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port),
                "AsyncTimeClientHandler").start();
    }
}
