package com.sunrise.netty.studyapi.customprotocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * @description: marshalling  编码器
 * @date: 2019/12/14
 * @author: lzhaoyang
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
        this.marshaller = MarshallingCodecFactory.buildMarshalling();
    }

    protected void encode(Object message, ByteBuf byteBuf) throws Exception {
        try {
            //可写索引
            int lengthPosition = byteBuf.writerIndex();
            byteBuf.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput channelBufferByteOutput = new ChannelBufferByteOutput(byteBuf);
            this.marshaller.start(channelBufferByteOutput);
            this.marshaller.writeObject(message);
            this.marshaller.finish();

            byteBuf.setInt(lengthPosition,byteBuf.writerIndex()-lengthPosition-4);
        }finally {
            this.marshaller.close();
        }
    }
}
