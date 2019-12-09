package com.sunrise.netty.studyapi.marshalling;

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
        SubscribeReq order = (SubscribeReq) msg;

        if (order.getUserName().equals("sunrise")){
            SubscribeRsp subscribeRsp = this.makeResponse(order);
            ctx.writeAndFlush(subscribeRsp);
        }else {
            SubscribeRsp subscribeRsp = new SubscribeRsp();
            subscribeRsp.setDesc("订单有误");
            ctx.writeAndFlush(subscribeRsp);
        }


    }

    private SubscribeRsp makeResponse(SubscribeReq req) {
        SubscribeRsp subscribeRsp = new SubscribeRsp();
        subscribeRsp.setDesc("Happy with netty world!");
        subscribeRsp.setStatusCode("200");
        subscribeRsp.setSubcribeId(req.getSubscribeId());
        return subscribeRsp;
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
