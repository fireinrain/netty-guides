package com.sunrise.netty.studyapi.tcpstick;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * @description: 注意： ChannelHandlerAdapter 在netty权威指南中是netty 5。0 的版本
 * 而现在官方发行的版本是4。x，5。0 是废弃的
 * 在4。x中ChannelHandlerAdapter 是 ChannelInboundHandlerAdapter
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/2 11:05 PM
 */
public class TimeClientChannelHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger(TimeClientChannelHandler.class.getName());

    private byte[] req;

    //用于消息计数
    private int counter;

    public TimeClientChannelHandler() {
        byte[] bytes = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
        this.req = bytes;
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
        ByteBuf message = null;
        //发送100次
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(this.req);
            ctx.writeAndFlush(message);
        }
        System.out.println("写入数据----成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive---->");

    }

    //这里是我们操作的主要区域
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         *         ByteBuf byteBuf = (ByteBuf) msg;
         *         byte[] bytes = new byte[byteBuf.readableBytes()];
         *         byteBuf.readBytes(bytes);
         *         //计数器+1
         *         this.counter++;
         *         System.out.println("Now is: " + new String(bytes, CharsetUtil.UTF_8) + "| counter：" + this.counter);
         */
        //解决客户端粘包拆包
        /**
         * 注意：
         *      收到的消息没有指定的结束标记。 比如指定了lineBasedFrameDecoder，没有换行标志，是不会调用channelRead方法的，其他的类似，
         *      所以在客户端和服务端发送的字符都是需要加上结束标记的，比如line separator
         */
        String message = (String) msg;

        //计数器+1
        this.counter++;
        System.out.println("Now is: " + message + "| counter：" + this.counter);
    }

    //这里也是
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读取数据完成---->");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.warning("发生错误：" + cause.getMessage());
        ctx.close();
    }
}
