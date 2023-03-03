package com.frame.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ServerInboundHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerInboundHandler1 channelActive --");
        super.channelActive(ctx);
    }

    /**
     * 通道有数据可读时
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("ServerInboundHandler1 channelRead--- -,remoteAddress={}", ctx.channel().remoteAddress());
        //处理接收的数据
        ByteBuf buf = (ByteBuf) msg;
        log.info("ServerInboundHandler1:received client data = {}", buf.toString(StandardCharsets.UTF_8));
        //将事件消息向下传递
        ctx.fireChannelRead(msg);
    }

    /**
     * 数据读取完毕时
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("ServerInboundHandler1 channelReadComplete----");
        ByteBuf buf = Unpooled.copiedBuffer("hello client, i am server", StandardCharsets.UTF_8);
        //通过ctx写，事件会从当前handler向pipeline头部移动
        ctx.writeAndFlush(buf);
        //通过Channel写,事件会从通道尾部向头部移动
//        ctx.channel().writeAndFlush(buf);
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
        log.info("ServerInboundHandler1 exceptionCaught--- -,cause={}", cause.getMessage());
    }
}