package com.sunrise.netty.studyapi.customprotocol.client;

import com.sunrise.netty.studyapi.customprotocol.codec.NettyMessageDecoder;
import com.sunrise.netty.studyapi.customprotocol.codec.NettyMessageEncoder;
import com.sunrise.netty.studyapi.customprotocol.constant.NettyMessageConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/19 10:20 PM
 */
public class NettyMessageClient {
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private EventLoopGroup group = new NioEventLoopGroup();


    public void conenct(String host, int port) throws InterruptedException {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // outbound
                            ch.pipeline().addLast("NettyMessageDecoder", new NettyMessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast("NettyMessageEncoder", new NettyMessageEncoder());
                            ch.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(10));
                            ch.pipeline().addLast("LoginAuthReqHandler", new LoginAuthReqHandler());
                            ch.pipeline().addLast("HeartBeatReqHandler", new HeartBeatReqHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(new InetSocketAddress(NettyMessageConstant.LOCALIP, NettyMessageConstant.LOCAL_PORT))
                    .sync()
                    .channel().connect(new InetSocketAddress(host, port))
                    .sync();
            ////远程地址

            //本地地址（客户端监听的地址）
            //NettyMessageConstant.REMOTEIP, NettyMessageConstant.PORT
            System.out.println("client connect to: " + NettyMessageConstant.PORT);
            future.channel().closeFuture().sync();

        } finally {
            this.executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    try {
                        conenct(NettyMessageConstant.REMOTEIP, NettyMessageConstant.PORT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyMessageClient().conenct(NettyMessageConstant.REMOTEIP, NettyMessageConstant.PORT);
    }


}
