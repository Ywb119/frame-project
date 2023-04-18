package com.frame.rpc.provider.netty.codec.encode;

import com.frame.rpc.provider.data.RpcResponse;
import com.frame.rpc.provider.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 21:47
 */
@Slf4j
public class RpcResponseEncoder extends MessageToMessageEncoder<RpcResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse response, List<Object> out) throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(response);
            ByteBuf buffer = ctx.alloc().buffer(bytes.length);
            buffer.writeBytes(bytes);
            out.add(buffer);
        } catch (Exception e) {
            log.error("RpcResponseEncoder encode error,msg={}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
