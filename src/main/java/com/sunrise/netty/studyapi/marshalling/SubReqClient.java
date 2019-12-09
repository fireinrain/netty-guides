package com.sunrise.netty.studyapi.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/4 10:22 PM
 */
public class SubReqClient {
    private static String host = "127.0.0.1";

    private static int port = 8888;

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new SubReqClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            System.out.println("client connect to: " + new InetSocketAddress(host, port).toString());
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅关闭
            group.shutdownGracefully();
        }
    }
}
