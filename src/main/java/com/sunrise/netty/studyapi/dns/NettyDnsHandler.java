package com.sunrise.netty.studyapi.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.dns.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/17 12:12 AM
 */
public class NettyDnsHandler extends SimpleChannelInboundHandler<DatagramDnsQuery> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramDnsQuery query) throws Exception {
        // 假数据，域名和ip的对应关系应该放到数据库中
        Map<String, byte[]> ipMap = new HashMap<>();
        ipMap.put("www.baidu.com.", new byte[]{61, (byte) 135, (byte) 169, 125});

        DatagramDnsResponse response = new DatagramDnsResponse(query.recipient(), query.sender(), query.id());
        try {
            DefaultDnsQuestion dnsQuestion = query.recordAt(DnsSection.QUESTION);
            response.addRecord(DnsSection.QUESTION, dnsQuestion);
            System.out.println("查询的域名：" + dnsQuestion.name());

            ByteBuf buf = null;
            if (ipMap.containsKey(dnsQuestion.name())) {
                buf = Unpooled.wrappedBuffer(ipMap.get(dnsQuestion.name()));
            } else {
                // TODO  对于没有的域名采用迭代方式
                // buf = Unpooled.wrappedBuffer(new byte[] { 127, 0, 0, 1});
            }
            // TTL设置为10s, 如果短时间内多次请求，客户端会使用本地缓存
            DefaultDnsRawRecord queryAnswer = new DefaultDnsRawRecord(dnsQuestion.name(), DnsRecordType.A, 10, buf);
            response.addRecord(DnsSection.ANSWER, queryAnswer);
            System.out.println("结果：" + response.content().toString());

        } catch (Exception e) {
            System.out.println("异常了：" + e);
        } finally {
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
