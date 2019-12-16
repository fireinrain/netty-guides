package com.sunrise.netty.studyapi.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/16 10:43 PM
 */
public class UdpClient {
    private static int port = 8888;
    private static String hostName = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new UdpClientHandler());

            Channel channel = bootstrap.bind(0)
                    .sync().channel();

            //udp 没有connect（连接）的概念
            DatagramPacket datagramPacket = new DatagramPacket(Unpooled.copiedBuffer("Hello udp!".getBytes()), new InetSocketAddress(hostName, port));
            channel.writeAndFlush(datagramPacket).sync();

            if (!channel.closeFuture().await(15_000)){
                System.out.println("接收数据超时！");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
