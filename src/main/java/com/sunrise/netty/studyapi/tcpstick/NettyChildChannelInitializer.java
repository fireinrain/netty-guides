package com.sunrise.netty.studyapi.tcpstick;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @description: 实现自己的worker group channel 处理器
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/2 10:05 PM
 */
public class NettyChildChannelInitializer extends ChannelInitializer<SocketChannel> {

    //添加管道处理
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("客户端："+socketChannel.remoteAddress()+"连接成功");
        //客户端解决tcp粘包和拆包问题 (LineBasedFrameDecoder,StringDecoder)
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new NettyTimeServerHandler());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("子通道初始化器被添加---->");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("子通道初始化器被移除---->");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("子通道初始化器被激活---->");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
