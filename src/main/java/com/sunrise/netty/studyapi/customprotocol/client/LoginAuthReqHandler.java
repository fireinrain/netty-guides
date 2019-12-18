package com.sunrise.netty.studyapi.customprotocol.client;

import com.sunrise.netty.studyapi.customprotocol.message.Header;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessage;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: 客户端握手接入
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/18 11:42 PM
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
    /**
     * 通道激活
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(this.buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;

        //如果是握手消息 需要判断是否认证成功
        if (nettyMessage.getHeader() != null
                && nettyMessage.getHeader().getType() == NettyMessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) nettyMessage.getBody();
            if (loginResult != (byte) 0) {
                //握手失败,连接关闭
                ctx.close();
            } else {
                System.out.println("Login is ok: " + nettyMessage);
                ctx.fireChannelRead(msg);
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private Object buildLoginReq() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(NettyMessageType.LOGIN_REQ.value());
        nettyMessage.setHeader(header);
        return nettyMessage;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }
}
