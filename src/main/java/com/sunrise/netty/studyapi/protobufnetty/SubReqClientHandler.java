package com.sunrise.netty.studyapi.protobufnetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
            SubscribeProto.SubscribeReq.Builder builder = SubscribeProto.SubscribeReq.newBuilder();
            //builder.setUserName("kk");
            builder.setUserName("sunrise");

            builder.setAddress("Moon");
            builder.setAddress("mars");
            builder.setProductName("macbookpro");
            builder.setSubscribeId(i);
            ctx.writeAndFlush(builder.build());
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeProto.SubscribeRsp body = (SubscribeProto.SubscribeRsp) msg;
        System.out.println("response is: " + new String(body.toByteArray(), CharsetUtil.UTF_8));
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
