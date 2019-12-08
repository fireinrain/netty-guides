package com.sunrise.netty.studyapi.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @description: 在普通场景下使用protobuf 序列化java class
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/8 8:29 PM
 */

public class TestProtobuf {


    //编码
    public static byte[] encode(SubscribeProto.SubscribeReq req){
        return req.toByteArray();
    }

    //解码
    public static SubscribeProto.SubscribeReq decode(byte[] bytes) throws InvalidProtocolBufferException {
        return SubscribeProto.SubscribeReq.parseFrom(bytes);
    }
    //创建
    public static SubscribeProto.SubscribeReq createSubscribeReq(){
        SubscribeProto.SubscribeReq.Builder builder = new SubscribeProto.SubscribeReq.Builder();
        builder.setSubscribeId(1);
        builder.setProductName("macbookpro 16");
        builder.setUserName("sunrise");
        builder.setAddress("mars");
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeProto.SubscribeReq subscribeReq = createSubscribeReq();
        System.out.println("before encode: \n"+subscribeReq.toString());
        byte[] encode = encode(subscribeReq);
        SubscribeProto.SubscribeReq decode = decode(encode);
        System.out.println("After encode and decode: \n"+decode.toString());
        System.out.println("Is equals: "+ encode.equals(subscribeReq));

    }
}
