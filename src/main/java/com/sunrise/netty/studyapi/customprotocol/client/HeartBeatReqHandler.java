package com.sunrise.netty.studyapi.customprotocol.client;

import com.sunrise.netty.studyapi.customprotocol.message.Header;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessage;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/19 9:18 PM
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage nettyMessage = this.buildHeartBeatMessage();
            System.out.println("--->client send heart beat message to server|: " + nettyMessage);
            this.ctx.writeAndFlush(nettyMessage);
        }

        private NettyMessage buildHeartBeatMessage() {
            NettyMessage nettyMessage = new NettyMessage();
            Header header = new Header();
            header.setType(NettyMessageType.HEARTBEAT_REQ.value());
            nettyMessage.setHeader(header);
            return nettyMessage;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == NettyMessageType.LOGIN_RESP.value()) {
            this.heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        } else if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == NettyMessageType.HEARTBEAT_RESP.value()) {
            System.out.println("<---client recieve server heart beat message|: " + nettyMessage);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (this.heartBeat!=null){
            this.heartBeat.cancel(true);
            this.heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
