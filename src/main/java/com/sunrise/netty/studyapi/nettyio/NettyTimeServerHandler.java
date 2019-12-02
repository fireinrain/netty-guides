package com.sunrise.netty.studyapi.nettyio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/2 10:09 PM
 */
public class NettyTimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //注意  这里是netty 自己实现的ByteBuf
        //ByteBuffer byteBuff = (ByteBuffer) msg;
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String recieveData = new String(bytes, CharsetUtil.UTF_8).trim();
        System.out.println("Time server revice: " + recieveData);
        //构造服务器响应
        String timeStr = "QUERY TIME ORDER".equalsIgnoreCase(recieveData) ? LocalDateTime.now().toString() : "BAD ORDER";
        //使用已有的字节数组构造缓冲区（注意netty 实现了自己的缓冲区类）
        //Unpooled 相当于是个工具类
        ByteBuf buffer = Unpooled.copiedBuffer(timeStr.getBytes());
        ctx.write(buffer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道处理器被添加<----");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道处理器被移除<----");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //出现异常 关闭通道连接
        ctx.close();
    }
}
