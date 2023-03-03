package com.frame.netty.server;

import com.frame.netty.pojo.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description
 * @author: ts
 * @create:2021-06-05 16:55
 */
@Slf4j
public class TcpStickHalfHandler1 extends ChannelInboundHandlerAdapter {

    int count =0;

    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,buf.toString(StandardCharsets.UTF_8));
        super.channelRead(ctx, msg);
    }*/


    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,message);
        super.channelRead(ctx, message);
    }*/

    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProto.Message message = (MessageProto.Message) msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,message);
    }*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserInfo userInfo = (UserInfo)msg;
        count++;
        log.info("---服务端收到的第{}个数据:{}",count,userInfo);
    }
}
