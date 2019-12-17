package com.sunrise.netty.studyapi.filetransfer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/17 11:14 PM
 */
public class NettyFileServer {
    private static int port = 8888;

    private static String hostName = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //接收客户端套接字的logger
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 200)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //执行顺序： inbound1-> inbound2 -> inbound3 -> outbound
                            ch.pipeline().addLast(
                                    //处理客户端套接字流程的logger
                                    new LoggingHandler(LogLevel.INFO),
                                    //outbound
                                    new StringEncoder(CharsetUtil.UTF_8),
                                    //inbound1
                                    new LineBasedFrameDecoder(1024),
                                    //inbound2
                                    new StringDecoder(CharsetUtil.UTF_8),
                                    //inbound3
                                    new NettyFileServerHandler()
                            );
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(hostName, port)).sync();
            System.out.println("NettyFileServer start at: " + hostName + ":" + port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
