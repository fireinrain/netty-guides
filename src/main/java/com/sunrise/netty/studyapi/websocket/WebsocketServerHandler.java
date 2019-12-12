package com.sunrise.netty.studyapi.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/12 9:17 PM
 */
public class WebsocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebsocketServerHandler.class.getName());

    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传统的HTTP请求
        if (msg instanceof FullHttpRequest){
            this.handleHttpRequest(ctx,(FullHttpRequest)msg);
        }else if (msg instanceof WebSocketFrame){
            this.handleWebsocketFrame(ctx,(WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //处理普通的HTTP请求
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        //需要注意的是，websocket在初次连接的时候发送的服务端的
        //是一个普通的HTTP请求，在头部有Upgrade标示，用来给服务端升级协议用的
        if (msg.uri().equals("/favicon.ico")){
            return;
        }
        if (!msg.decoderResult().isSuccess()) {
            //如果解码失败
            this.sendHttpResonse(ctx, msg, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;

        }
        if (msg.decoderResult().isSuccess()){
            //如果是头部有Upgrade标示
            if (("websocket".equals(msg.headers().get("Upgrade")))){
                //构造握手响应
                WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(
                        "ws://localhost:8888/websocket", null, false
                );
                this.webSocketServerHandshaker = webSocketServerHandshakerFactory.newHandshaker(msg);
                if (this.webSocketServerHandshaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    this.webSocketServerHandshaker.handshake(ctx.channel(), msg);
                }
            }else{
                //是普通的HTTP，则返回，祝你好运
                DefaultFullHttpResponse okRep = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                this.sendHttpResonse(ctx,msg,okRep);
            }
        }

    }

    //处理websocket消息
    private void handleWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        //判断是否关闭链路的指令
        if (msg instanceof CloseWebSocketFrame){
            this.webSocketServerHandshaker.close(ctx.channel(),(CloseWebSocketFrame) msg.retain());
            return;
        }
        //判断是否是ping信息
        if (msg instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
        }
        //只处理文本消息，不支持二进制消息
        if(!(msg instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame type unsupported",msg.getClass().getName()));
        }

        //返回消息
        String text = ((TextWebSocketFrame) msg).text();
        if (logger.isLoggable(Level.FINE)){
            logger.fine(String.format("%s recieved %s",ctx.channel(),text));
        }
        ctx.channel().write(new TextWebSocketFrame(text+" : 欢迎访问netty websocket服务，当前时间是： "+ LocalDateTime.now().toString()));
    }


    private void sendHttpResonse(ChannelHandlerContext ctx, FullHttpRequest msg, DefaultFullHttpResponse defaultFullHttpResponse) {
        //如果响应不是200
        if (defaultFullHttpResponse.status().code() != 200){
            ByteBuf byteBuf = Unpooled.copiedBuffer(defaultFullHttpResponse.status().toString(), CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(byteBuf);
            byteBuf.release();
            HttpUtil.setContentLength(defaultFullHttpResponse,defaultFullHttpResponse.content().readableBytes());
        }
        //响应是200 状态
        if (defaultFullHttpResponse.status().code() == 200){
            //获取访问的路径，给他写上祝福语 + 路径 返回回去
            String uri = msg.uri();

            ByteBuf byteBuf = Unpooled.copiedBuffer("你访问的路径是： "+uri, CharsetUtil.UTF_8);

            defaultFullHttpResponse.content().writeBytes(byteBuf);

            byteBuf.release();
            HttpUtil.setContentLength(defaultFullHttpResponse,defaultFullHttpResponse.content().readableBytes());
            defaultFullHttpResponse.headers().set("Content-Type","text/html; charset=utf-8");

            logger.info("------>"+defaultFullHttpResponse.content().toString());

        }

        ChannelFuture channelFuture = ctx.channel().writeAndFlush(defaultFullHttpResponse);
        //如果是非keep-alive
        if(!HttpUtil.isKeepAlive(msg) || defaultFullHttpResponse.status().code() != 200){
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
