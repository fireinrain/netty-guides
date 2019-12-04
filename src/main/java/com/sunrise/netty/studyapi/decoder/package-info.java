package com.sunrise.netty.studyapi.decoder;

//界定符解码器和定长解码器的使用
//DelimiterBasedDecoder and FixedLengthBasedDecoder

//如果给定的数据在解码时不符合解码器的要求，那么会出现channelRead 无法调用的情况

//比如如果使用DelimiterBasedDecoder，而你发送的数据没有加上界定符




//使用FixedLengthBasedDecoder，指定了帧的数据长度（字节），而你发送的数据没有达到指定的字节数量
/*
client：
    Trying 127.0.0.1...
    Connected to localhost.
    Escape character is '^]'.
    xxxxxxxxxxxxxxxxxxxxa   // 发送21 字节的数据
    xxxxxxxxxxxxxxxxxxxx    //只收到20 字节，说明定长解码器是起作用的



Echo server start: 8888
Dec 04, 2019 10:58:56 PM io.netty.handler.logging.LoggingHandler channelRead
INFO: [id: 0xc37b2b42, L:/0:0:0:0:0:0:0:0:8888] READ: [id: 0xfbb8aa4d, L:/127.0.0.1:8888 - R:/127.0.0.1:60797]
Dec 04, 2019 10:58:56 PM io.netty.handler.logging.LoggingHandler channelReadComplete
INFO: [id: 0xc37b2b42, L:/0:0:0:0:0:0:0:0:8888] READ COMPLETE
开启定长解码器，长度20个字节
this counter： 1 message body：xxxxxxxxxxxxxxxxxxxx
 */