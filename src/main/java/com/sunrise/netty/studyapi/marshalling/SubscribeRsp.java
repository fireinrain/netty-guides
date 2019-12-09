package com.sunrise.netty.studyapi.marshalling;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/8 9:39 PM
 */
@Data
public class SubscribeRsp implements Serializable {
    private int subcribeId;

    private String statusCode;

    private String desc;

}
