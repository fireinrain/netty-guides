package com.sunrise.netty.studyapi.serialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/4 10:30 PM
 */
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {


    //连接完成
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            SubscribeReq subscribeReq = new SubscribeReq();
            subscribeReq.setUserName("sunrise");
            subscribeReq.setAddress("Moon");
            subscribeReq.setPhoneNumber("88888888888");
            subscribeReq.setProductName("macbookpro");
            subscribeReq.setSubReqId(i);
            ctx.writeAndFlush(subscribeReq);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeRsp body = (SubscribeRsp) msg;
        System.out.println("response is: " + body);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
