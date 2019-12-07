package com.sunrise.netty.studyapi.serialization;

import java.io.Serializable;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/5 10:01 PM
 */
public class SubscribeRsp implements Serializable {
    private static final long serialVersionUID = 3738622813376291799L;

    //订单id
    private int subReqId;

    //状态码
    private int rspCode;

    //描述
    private String desc;

    public int getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(int subReqId) {
        this.subReqId = subReqId;
    }

    public int getRspCode() {
        return rspCode;
    }

    public void setRspCode(int rspCode) {
        this.rspCode = rspCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeRsp{" +
                "subReqId=" + subReqId +
                ", rspCode=" + rspCode +
                ", desc='" + desc + '\'' +
                '}';
    }
}
