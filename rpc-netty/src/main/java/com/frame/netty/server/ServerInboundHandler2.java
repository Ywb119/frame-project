package com.frame.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
//共享
//@ChannelHandler.Sharable
public class ServerInboundHandler2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerInboundHandler2 channelActive----- ");
        ctx.fireChannelActive();
    }

    /**
     * 通道有数据可读时
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) throws Exception {
        log.info("ServerInboundHandler2 channelRead--- -,remoteAddress={}", ctx.channel().remoteAddress());
        //处理接收的数据
        ByteBuf buf = (ByteBuf) msg;
        log.info("ServerInboundHandler2:received client data = {}", buf.toString(StandardCharsets.UTF_8));
        ctx.fireChannelActive();
    }

    /**
     * 数据读取完毕时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerInboundHandler2 channelReadComplete----");
        ctx.fireChannelActive();
    }

    /**
     * 发生异常时
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("ServerInboundHandler2 exceptionCaught--- -,cause={}", cause.getMessage());
    }
}
