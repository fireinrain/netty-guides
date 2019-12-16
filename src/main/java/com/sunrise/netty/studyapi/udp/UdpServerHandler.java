package com.sunrise.netty.studyapi.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/16 10:28 PM
 */
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {

        //需要注意的是这里的DatagramPacket  是netty 自己封装的，并不是jdk 自带的 自带的DatagramPacket
        System.out.println("接收到："+msg.content().toString(CharsetUtil.UTF_8));
        DatagramPacket datagramPacket = new DatagramPacket(Unpooled.copiedBuffer((">>>你输入的是：" + msg.content().toString(CharsetUtil.UTF_8)).getBytes()),
                msg.sender());
        ctx.writeAndFlush(datagramPacket);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
