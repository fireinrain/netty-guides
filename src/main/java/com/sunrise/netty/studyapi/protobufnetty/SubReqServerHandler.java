package com.sunrise.netty.studyapi.protobufnetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/4 10:10 PM
 */
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeProto.SubscribeReq order = (SubscribeProto.SubscribeReq) msg;

        if (order.getUserName().equals("sunrise")){
            SubscribeProto.SubscribeRsp subscribeRsp = this.makeResponse(order);
            ctx.writeAndFlush(subscribeRsp);
        }else {
            SubscribeProto.SubscribeRsp.Builder subscribeRsp =  SubscribeProto.SubscribeRsp.newBuilder();
            subscribeRsp.setDesc("订单有误");
            SubscribeProto.SubscribeRsp rsp = subscribeRsp.build();
            ctx.writeAndFlush(rsp);
        }


    }

    private SubscribeProto.SubscribeRsp makeResponse(SubscribeProto.SubscribeReq req) {
        SubscribeProto.SubscribeRsp.Builder builder = SubscribeProto.SubscribeRsp.newBuilder();
        builder.setDesc("Order success!");
        builder.setStatusCode("200");
        builder.setSubscribeId(req.getSubscribeId());
        return builder.build();
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
