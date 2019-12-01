package com.sunrise.netty.studyapi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 5:51 PM
 */
public class SelectTimeServer implements Runnable {
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private boolean stopFlag;

    @Override
    public void run() {
        while (!this.stopFlag) {
            try {
                this.selector.select(1000);
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //及时删除 标记为处理
                    iterator.remove();
                    try {
                        //处理
                        handleSelectKey(selectionKey);

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (selector != null) {
                    try {
                        this.selector.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 处理触发的事件
     *
     * @param selectionKey
     */
    private void handleSelectKey(SelectionKey selectionKey) {
        //如果这个key 是有效的
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel selectableChannel = (ServerSocketChannel) selectionKey.channel();
            try {
                SocketChannel socketChannel = selectableChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            } catch (IOException e) {
                selectionKey.cancel();
                try {
                    selectionKey.channel().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        //如果可读
        if (selectionKey.isReadable()) {
            SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            try {
                int readData = clientChannel.read(byteBuffer);
                if (readData > 0) {
                    byteBuffer.flip();
                    //用个数组来接收一下
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    //填充好给定的数组
                    byteBuffer.get(bytes);
                    String recieveData = new String(bytes, "UTF-8").trim();
                    System.out.println("recieve from client: " + recieveData);

                    //给客户端回写数据
                    byteBuffer.clear();
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(recieveData) ? LocalDateTime.now().toString() :
                            "BAD ORDER";
                    byteBuffer.put(currentTime.getBytes());
                    selectionKey.attach(byteBuffer);
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                } else if (readData < 0) {
                    //链路关闭
                    selectionKey.cancel();
                    clientChannel.close();
                } else {
                    //pass 读到0 字节 忽略
                }
            } catch (IOException e) {
                selectionKey.cancel();
                try {
                    selectionKey.channel().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        //如果可写
        if (selectionKey.isWritable()) {
            ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
            //可能这个attach 并不存在
            //如果一端的Socket被关闭（或主动关闭，或因为异常退出而 引起的关闭），
            // 另一端仍发送数据，发送的第一个数据包引发该异常(Connect reset by peer)。
            if (attachment!=null){
                attachment.flip();
                SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                try {
                    clientChannel.write(attachment);
                    clientChannel.close();
                } catch (IOException e) {
                    selectionKey.cancel();
                    try {
                        selectionKey.channel().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public SelectTimeServer(int port) {
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("nio server is on: " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stopFlag = true;
    }
}
