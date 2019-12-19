package com.sunrise.netty.studyapi.customprotocol.server;

import com.sunrise.netty.studyapi.customprotocol.codec.NettyMessageDecoder;
import com.sunrise.netty.studyapi.customprotocol.codec.NettyMessageEncoder;
import com.sunrise.netty.studyapi.customprotocol.constant.NettyMessageConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/19 11:22 PM
 */
public class NettyMessageServer {
    public void start(){

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("NettyMessageDecoder",new NettyMessageDecoder(1024*1024,4,4));
                            ch.pipeline().addLast("NettyMessageEncoder",new NettyMessageEncoder());
                            ch.pipeline().addLast("ReadTimeoutHandler",new ReadTimeoutHandler(10));
                            ch.pipeline().addLast("LoginAuthRespHandler",new LoginAuthRespHandler());
                            ch.pipeline().addLast("HeartBeatRespHandler",new HeartBeatRespHandler());

                        }
                    });
            ChannelFuture future = serverBootstrap.bind(NettyMessageConstant.REMOTEIP,NettyMessageConstant.PORT).sync();
            System.out.println("NettyMessage server start: " + new InetSocketAddress(NettyMessageConstant.REMOTEIP,NettyMessageConstant.PORT));
            future.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new NettyMessageServer().start();
    }
}
