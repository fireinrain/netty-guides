package com.sunrise.netty.studyapi.filetransfer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/17 11:18 PM
 */
public class NettyFileServerHandler extends SimpleChannelInboundHandler<String> {
    private static String fileSep = File.separator;

    private static String dirRoot = "src/main/java/com/sunrise/netty/studyapi/filetransfer";

    private static String lineSep = System.getProperty("line.separator");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Path path = Paths.get(dirRoot + fileSep + msg);
        if (Files.exists(path)) {
            //不是普通文件不处理
            if (!Files.isRegularFile(path)) {
                ctx.writeAndFlush("Not a file: " + msg + lineSep);
                return;
            }
            //普通文件
            FileChannel fileChannel = FileChannel.open(path);
            long size = fileChannel.size();
            //写入文件描述
            ctx.write("File: " + msg + " Length: " + size + " bytes" + lineSep);
            FileRegion fileRegion = new DefaultFileRegion(fileChannel, 0, size);
            ctx.write(fileRegion);
            //写入换行标志（解决tcp粘包拆包）
            ctx.writeAndFlush(lineSep);
            fileChannel.close();

        } else {
            ctx.writeAndFlush("Not found: " + msg + lineSep);
            return;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
