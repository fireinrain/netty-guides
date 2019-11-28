package com.sunrise.netty.studyapi.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 传统的阻塞式IO
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/11/28 10:08 PM
 */
public class TimeServer {
    public static int port = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("Timeserver listen at port: "+port);
            while (true){
                Socket accept = serverSocket.accept();
                System.out.println("recieve new conenction: "+accept.getInetAddress()+":"+accept.getPort());
                //接收到客户端的连接请求
                new Thread(new TimeServerHandler(accept)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
