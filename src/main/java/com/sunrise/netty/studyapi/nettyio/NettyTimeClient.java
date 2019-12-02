package com.sunrise.netty.studyapi.nettyio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @description: netty timeclient
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/2 10:50 PM
 */
public class NettyTimeClient {
    private static int port = 8888;
    private static String host = "127.0.0.1";


    public static void main(String[] args) {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ClientChannelInitializer());
            //发起异步连接操作
            ChannelFuture future = bootstrap.connect().sync();
            //异步等待关闭通道
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            clientGroup.shutdownGracefully();
        }
    }
}
