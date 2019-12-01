package com.sunrise.netty.studyapi.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 8:18 PM
 */
public class NioTimeHandler implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private boolean stopFlag;

    public NioTimeHandler(String host, int port) {
        this.stopFlag = false;
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            this.selector = selector;
            this.socketChannel = socketChannel;
            this.socketChannel.configureBlocking(false);
            this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            this.socketChannel.connect(new InetSocketAddress(this.host, this.port));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!this.stopFlag) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    handleKey(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (selector != null) {
            try {
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleKey(SelectionKey selectionKey) {
        try {
            if (selectionKey.isValid()) {
                //判断是否连接成功
                if (selectionKey.isConnectable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //如果客户端完成了对服务端的连接
                    if (channel.finishConnect()) {
                        System.out.println("客户端连接成功：" + channel.getRemoteAddress());
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byte[] bytes = "QUERY TIME ORDER".getBytes();
                        byteBuffer.put(bytes);
                        byteBuffer.flip();
                        selectionKey.attach(byteBuffer);
                        selectionKey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    }
                }
                //如果是可读状态
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                    int readData = channel.read(attachment);
                    if (readData > 0) {
                        attachment.flip();
                        byte[] bytes = new byte[attachment.remaining()];
                        attachment.get(bytes);
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        System.out.println("Now is: " + body);
                        //读取到了服务端的数据，所以退出
                        //this.stopFlag = true;
                    } else if (readData < 0) {
                        selectionKey.cancel();
                        channel.close();
                    } else {
                        //读到0 字节，说明数据还没来
                        //pass
                    }
                }
                //如果是可写状态
                if (selectionKey.isWritable()) {
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();

                    channel.write(attachment);
                    if (!attachment.hasRemaining()) {
                        System.out.println("send order 2 server success!");
                    }
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        } catch (Exception e) {
            if (selectionKey != null) {
                selectionKey.cancel();
                if (selectionKey.channel() != null) {
                    try {
                        selectionKey.channel().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
