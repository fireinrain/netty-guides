package com.sunrise.netty.studyapi.httpserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/10 10:04 PM
 */
public class FileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String defaultUrl;

    public FileServerHandler(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //如果解码不成功
        if (!request.decoderResult().isSuccess()) {
            this.sendError(ctx, BAD_REQUEST);
            return;
        }
        //如果请求方法不匹配
        if (!request.method().equals(GET)) {
            this.sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        String uri = request.uri();


    }

    /**
     * 发送错误信息
     *
     * @param ctx
     * @param badRequest
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus badRequest) {

    }


}
