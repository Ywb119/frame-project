package com.frame.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientInboundHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        log.info("ClientInboundHandler1 channelActive begin send data");
        //通道准备就绪后开始向服务端发送数据
        ByteBuf buf = Unpooled.copiedBuffer("hello server,i am client".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("ClientInboundHandler1 channelRead");
        ByteBuf buf = (ByteBuf) msg;
        log.info("ClientInboundHandler1: received server data ={}",buf.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }



}
