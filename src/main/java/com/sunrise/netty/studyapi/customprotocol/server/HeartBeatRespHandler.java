package com.sunrise.netty.studyapi.customprotocol.server;

import com.sunrise.netty.studyapi.customprotocol.message.Header;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessage;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/19 9:54 PM
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        //如果接收到请求心跳
        if (nettyMessage.getHeader()!=null && nettyMessage.getHeader().getType()== NettyMessageType.HEARTBEAT_REQ.value()){
            System.out.println("<---Recieve  heat beat message from client|: "+nettyMessage);
            //响应心跳
            NettyMessage nettyMessage1 = this.buildHeartHeatMessage();
            System.out.println("--->Send heat beat response message to client|: "+nettyMessage1);
            ctx.writeAndFlush(nettyMessage1);

        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartHeatMessage() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(NettyMessageType.HEARTBEAT_RESP.value());
        nettyMessage.setHeader(header);
        return nettyMessage;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
