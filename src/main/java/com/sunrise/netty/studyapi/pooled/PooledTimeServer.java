package com.sunrise.netty.studyapi.pooled;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 线程池化的IO，n:m n线程池线程，m任务数量
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 4:29 PM
 */
public class PooledTimeServer {
    private static int port = 9999;

    private static PooledExcutor executor = new PooledExcutor(50,2000);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true){
                Socket accept = serverSocket.accept();
                executor.excute(new TimeServerHandler(accept));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
