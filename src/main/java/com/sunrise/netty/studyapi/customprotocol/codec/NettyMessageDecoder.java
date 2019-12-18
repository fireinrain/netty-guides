package com.sunrise.netty.studyapi.customprotocol.codec;

import com.sunrise.netty.studyapi.customprotocol.message.Header;
import com.sunrise.netty.studyapi.customprotocol.message.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * @description: netty 消息解码器
 * @date: 2019/12/14
 * @author: lzhaoyang
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    private MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        //获取附件attachment map大小
        int size = in.readInt();
        if (size > 0) {
            HashMap<String, Object> attach = new HashMap(size);
            //map 中 key的长度，因为key是一个字符串，所以需要记录他的长度
            int keySize = 0;
            //key的字节数据
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = in.readInt();
                keyArray = new byte[keySize];
                in.readBytes(keyArray);
                key = new String(keyArray, CharsetUtil.UTF_8);
                attach.put(key,marshallingDecoder.decode(in));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attach);


        }
        if (in.readableBytes()>4){
            nettyMessage.setBody(marshallingDecoder.decode(in));
        }
        nettyMessage.setHeader(header);
        return nettyMessage;
    }
}
