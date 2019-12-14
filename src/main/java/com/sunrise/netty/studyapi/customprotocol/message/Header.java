package com.sunrise.netty.studyapi.customprotocol.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @date: 2019/12/14
 * @author: lzhaoyang
 */
public class Header {
    //校验码
    private int crcCode = 0xabef0101;
    //消息长度
    private int length;
    //会话ID
    private long sessionID;
    //消息类型
    private byte type;
    //优先级
    private byte priority;
    //拓展附件
    private Map<String,Object> attachment = new HashMap<>();

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
