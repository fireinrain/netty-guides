package com.sunrise.netty.studyapi.customprotocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/18 10:48 PM
 */
public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception{
        //获取大小
        int objectSize = in.readInt();
        //截取
        ByteBuf byteBuf = in.slice(in.readerIndex(), objectSize);
        ChannelBufferByteInput byteInput = new ChannelBufferByteInput(byteBuf);
        try {
            unmarshaller.start(byteInput);
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            //标记为读完了
            in.readerIndex(in.readerIndex()+objectSize);
            return object;
        }finally {
            unmarshaller.close();
        }
    }
}
