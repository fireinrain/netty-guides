package com.sunrise.netty.studyapi.codeapi;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/22 11:04 PM
 */
public class NettyByteBuf {
    public static void main(String[] args) {
        //ByteBuf 是抽象类
        //ByteBuf byteBuf = new ByteBuf();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[10]);

        //java nio 自带的ByteBuffer
        ByteBuffer allocate = ByteBuffer.allocate(10);
    }
}
