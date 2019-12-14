package com.sunrise.netty.studyapi.customprotocol;


//使用netty 自定义协议

/**
 *  消息头 + 消息体
 *
 *
 *  名称      类型        长度          ，描述
 *  header    Header      变长            消息头定义
 *  body      Object      变长            对于请求消息   他是方法参数/ 对于响应消息  他是返回值
 *
 * 消息头定义 Header
 *
 *  crcCode   int          32             netty消息的校验码 他由三部分组成
 *                                          1. 0xABEF  固定值 表示该消息是netty协议消息 2个字节
 *                                          2. 主版本号： 1-255 一个字节
 *                                          3. 次版本号： 1-244 一个字节
 *                                          crcCode =  0xAEF + 主版本号+ 次版本号
 * length     int           32              消息长度 整个消息  包括消息头  消息体
 *
 * sessionID   long          64              集群节点内全局唯一，由会话ID生成器生成
 *
 * type         Byte          8               0： 业务请求消息
 *                                            1： 业务响应消息
 *                                            2:  业务ONE WAY 消息（既是请求又是响应消息）
 *                                            3： 握手请求消息
 *                                            4： 握手应答消息
 *                                            5： 心跳请求消息
 *                                            6： 心跳应答消息
 *
 * priority     Byte         8                消息优先级
 *
 * attachment   Map<String Object>  变成       可选字段  用于扩展消息头
 *
 *
 *
 */

// 可靠性设计
// 1. 心跳机制
// 2. 重连机制
// 3. 重复登录保护
// 4. 消息缓存重发