package com.frame.rpc.provider.netty.client;

import com.frame.rpc.provider.netty.pojo.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientInboundHandler1 extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        log.info("ClientInboundHandler1 channelActive begin send data");
        //批量发送数据
        UserInfo userInfo;
        for (int i = 0; i < 100; i++) {
            userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");
            String msg = userInfo.toString();
            ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes(StandardCharsets.UTF_8)));
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("ClientInboundHandler1 channelRead");
        ByteBuf buf = (ByteBuf) msg;
        log.info("ClientInboundHandler1: received server data ={}", buf.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }


}
