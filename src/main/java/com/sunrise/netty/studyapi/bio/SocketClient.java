package com.sunrise.netty.studyapi.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/11/29 12:24 AM
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message="你好  yiwangzhibujian";
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        outputStream.close();
        socket.close();

    }
}
