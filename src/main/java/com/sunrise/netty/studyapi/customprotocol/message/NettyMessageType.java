package com.sunrise.netty.studyapi.customprotocol.message;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/18 11:46 PM
 */
public enum NettyMessageType {
    //请求
    SERVICE_REQ((byte) 0),
    //响应
    SERVICE_RESP((byte) 1),
    //
    ONE_WAY((byte) 2),
    //认证请求
    LOGIN_REQ((byte) 3),
    //认证响应
    LOGIN_RESP((byte) 4),
    //心跳请求
    HEARTBEAT_REQ((byte) 5),
    //心跳响应
    HEARTBEAT_RESP((byte) 6);

    private byte value;

    private NettyMessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
