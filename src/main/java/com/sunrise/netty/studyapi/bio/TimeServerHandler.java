package com.sunrise.netty.studyapi.bio;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * @description: 处理与客户端的连接
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/11/28 10:13 PM
 */
public class TimeServerHandler implements Runnable {
    public Socket clientSocket;

    public TimeServerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        //获取输入输出流
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            printWriter = new PrintWriter(outputStream, true);

            String currentTime = null;
            int c;
            //需要注意一下几点
            // 1 read 什么是否返回-1 是读取到EOF 的时候
            // 2 客户端发送的字符串 结尾带着 \n,是带有行尾的
            StringBuilder stringBuilder = new StringBuilder();
            while ((c = bufferedReader.read()) != -1) {
                //读取流
                stringBuilder.append((char) c);
            }
            System.out.println("The TimeServer recieve order: " + stringBuilder.toString());
            String string = stringBuilder.toString();
            System.out.println("str: "+string);
            if (string.equals("query time order\n")){
                currentTime = LocalDateTime.now().toString();
            }else {
                currentTime = "BAD ORDER";
            }
            //printLn 这个方法会给字符串自动加上行为
            printWriter.println(currentTime);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
