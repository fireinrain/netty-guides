package com.sunrise.netty.studyapi.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/16 10:22 PM
 */
public class UdpServer {
    private static int port = 8888;
    private static String hostName = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new UdpServerHandler());

            System.out.println("udp server listen at: "+new InetSocketAddress(hostName,port).toString());
            bootstrap.bind(new InetSocketAddress(hostName,port))
                    .sync().channel().closeFuture().await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }

}
