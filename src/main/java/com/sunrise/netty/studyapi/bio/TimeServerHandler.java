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
            String body = null;
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while ((c = bufferedReader.read())!=-1) {
                //读取流
                stringBuilder.append((char)c);
            }
            System.out.println("The TimeServer recieve order: " + body);
            currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? LocalDateTime.now().toString() : "BAD ORDER";
            printWriter.write(currentTime);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (clientSocket!=null){
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
