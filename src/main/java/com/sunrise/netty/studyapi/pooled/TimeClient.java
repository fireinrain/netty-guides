package com.sunrise.netty.studyapi.pooled;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @description: 客户端
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/11/28 11:26 PM
 */
public class TimeClient {
    public static int port = 9999;

    public static void main(String[] args) throws IOException {

        runManeyClient(5000);

    }
    public static void task(){
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed.");
            String resp = in.readLine();
            System.out.println("Now is : " + resp);
            Thread.sleep(5_0000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void runManeyClient(int clientCount) {
        for (int i = 0; i <clientCount ; i++) {
            Runnable runnable = () -> task();
            new Thread(runnable).start();
        }

    }
}
