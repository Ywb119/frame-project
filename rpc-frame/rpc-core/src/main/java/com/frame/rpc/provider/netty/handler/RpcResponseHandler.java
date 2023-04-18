package com.frame.rpc.provider.netty.handler;


import com.frame.rpc.provider.data.RpcResponse;
import com.frame.rpc.provider.netty.request.RequestPromise;
import com.frame.rpc.provider.netty.request.RpcRequestHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        log.info("---rpc调用成功,返回的结果:{}",response);
        RequestPromise requestPromise = RpcRequestHolder.getRequestPromise(response.getRequestId());
        if (requestPromise!=null) {
            // 设置返回结果 并出发监听回调
            requestPromise.setSuccess(response);
        }
    }
}
