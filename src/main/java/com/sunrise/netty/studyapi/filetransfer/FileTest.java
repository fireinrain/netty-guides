package com.sunrise.netty.studyapi.filetransfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/17 10:33 PM
 */
public class FileTest {
    public static void main(String[] args) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("src/main/java/com/sunrise/netty/studyapi/filetransfer/package-info.java", "rw");
            FileChannel channel = randomAccessFile.getChannel();

            ByteBuffer allocate = ByteBuffer.allocate(1024);
            StringBuilder stringBuilder = new StringBuilder();
            int index;
            while ((index = channel.read(allocate))!=-1){
                allocate.flip();
                byte[] array = allocate.array();
                stringBuilder.append(new String(array,0,index));
                allocate.clear();
            }
            System.out.println(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
