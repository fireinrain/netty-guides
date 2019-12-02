package com.sunrise.netty.studyapi.nettyio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

/**
 * @description:
 *
 * 注意： ChannelHandlerAdapter 在netty权威指南中是netty 5。0 的版本
 *    而现在官方发行的版本是4。x，5。0 是废弃的
 *    在4。x中ChannelHandlerAdapter 是 ChannelInboundHandlerAdapter
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/2 11:05 PM
 */
public class TimeClientChannelHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(TimeClientChannelHandler.class.getName());

    private  final ByteBuf byteBuf;

    public TimeClientChannelHandler() {
        byte[] bytes = "QUERY TIME ORDER".getBytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        this.byteBuf = byteBuf;
    }

    //我们这里特意重写一些方法 来了解执行过程
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered---->");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered---->");
    }

    //连接成功写数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive---->");
        ctx.writeAndFlush(this.byteBuf);
        System.out.println("写入数据----成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive---->");

    }

    //这里是我们操作的主要区域
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.out.println("Now is: "+new String(bytes, CharsetUtil.UTF_8));
    }

    //这里也是
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读取数据完成---->");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.warning("发生错误："+cause.getMessage());
        ctx.close();
    }
}
