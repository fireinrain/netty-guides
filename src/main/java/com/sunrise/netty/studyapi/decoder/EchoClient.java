package com.sunrise.netty.studyapi.decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/4 10:22 PM
 */
public class EchoClient {
    private static String host = "127.0.0.1";

    private static int port = 8888;

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    //启动TCP_NODELAY，就意味着禁用了Nagle算法，允许小包的发送
                    //对于关闭TCP_NODELAY，则是应用了Nagle算法。数据只有在写缓存中累积到一定量之后，
                    // 才会被发送出去，这样明显提高了网络利用率（实际传输数据payload与协议头的比例大大提高）。但是这由不可避免地增加了延时
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiterBuff = Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiterBuff));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            System.out.println("client connect to: " + new InetSocketAddress(host, port).toString());
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅关闭
            group.shutdownGracefully();
        }
    }
}
