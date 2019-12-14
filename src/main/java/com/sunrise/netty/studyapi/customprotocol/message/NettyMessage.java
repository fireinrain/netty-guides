package com.sunrise.netty.studyapi.customprotocol.message;

import com.sunrise.netty.studyapi.customprotocol.message.Header;

/**
 * @description: 消息实体
 * @date: 2019/12/14
 * @author: lzhaoyang
 */
public class NettyMessage {
    //消息头
    private Header header;

    //消息体
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
