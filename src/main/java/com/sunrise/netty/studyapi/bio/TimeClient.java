package com.sunrise.netty.studyapi.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @description: 客户端
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/11/28 11:26 PM
 */
public class TimeClient {
    public static int port = 8888;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", port);
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            printWriter.println("query time order");
            ////通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            //会让对方的socket 感知到EOF
            socket.shutdownOutput();
            System.out.println("send order to server success");
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while ((c = bufferedReader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            System.out.println("Time is now: " + stringBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printWriter.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
