package com.frame.rpc.provider.netty.codec.encode;

import com.frame.rpc.provider.data.RpcResponse;
import com.frame.rpc.provider.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameResponseEncoder extends MessageToMessageEncoder<RpcResponse> {

    /**
     * 二次编码器
     */
    @Override
    protected void encode(ChannelHandlerContext context, RpcResponse rpcResponse, List<Object> out) throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(rpcResponse);
            ByteBuf byteBuf = context.alloc().buffer(bytes.length);
            byteBuf.writeBytes(byteBuf);
            out.add(byteBuf);
        } catch (Exception e) {
            log.error("RpcResponse encode error ,msg={}", e.getMessage());
        }
    }
}
