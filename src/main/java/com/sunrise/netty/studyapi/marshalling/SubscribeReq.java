package com.sunrise.netty.studyapi.marshalling;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/8 9:31 PM
 */
@Data
public class SubscribeReq implements Serializable{
    private static final long serialVersionUID = -4341230404196501079L;

    private int subscribeId;

    private String userName;

    private String productName;

    private String address;

}
