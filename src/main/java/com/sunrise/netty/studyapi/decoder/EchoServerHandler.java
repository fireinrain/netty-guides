package com.sunrise.netty.studyapi.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/4 10:10 PM
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    //消息计数
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        this.counter++;
        System.out.println("this counter： " + counter + " message body：" + message);
        //界定符解码器测试
        //String responseStr = message + "$_";

        //定长解码器测试
        String responseStr = message;

        ByteBuf byteBuf = Unpooled.wrappedBuffer(responseStr.getBytes());
        ctx.writeAndFlush(byteBuf);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
