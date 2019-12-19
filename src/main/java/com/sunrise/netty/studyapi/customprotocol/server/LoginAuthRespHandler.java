package com.sunrise.netty.studyapi.customprotocol.server;

import com.sunrise.netty.studyapi.customprotocol.message.Header;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessage;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 服务端握手接入
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/19 12:33 AM
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();

    private String[] whiteList = {"127.0.0.1", "192.168.1.114"};


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        //如果是握手请求消息则处理，其他则透传
        if (nettyMessage.getHeader() != null
                && nettyMessage.getHeader().getType() == NettyMessageType.LOGIN_REQ.value()) {
            String ipaddr = ctx.channel().remoteAddress().toString();
            NettyMessage loginRsp = null;
            if (nodeCheck.containsKey(ipaddr)) {
                loginRsp = this.buildLoginRsp((byte) -1);
            } else {
                InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                String hostAddress = socketAddress.getAddress().getHostAddress();
                boolean isOK = false;
                for (String ip : whiteList) {
                    if (ip.equals(hostAddress)) {
                        isOK = true;
                        break;
                    }
                }
                loginRsp = isOK ? this.buildLoginRsp((byte) 0) : buildLoginRsp((byte) -1);
                if (isOK) {
                    nodeCheck.put(ipaddr, true);
                }

            }
            System.out.println("###：The login response is : " + loginRsp
                    + " body [" + loginRsp.getBody() + "]");
            ctx.writeAndFlush(loginRsp);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildLoginRsp(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(NettyMessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //删除出现异常的ip
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        System.out.println("--->: 移除掉线客户端ip");
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    public static void main(String[] args) {
        InetSocketAddress unresolved = InetSocketAddress.createUnresolved("127.0.0.1", 4444);
        System.out.println(unresolved.getHostName());
    }
}
