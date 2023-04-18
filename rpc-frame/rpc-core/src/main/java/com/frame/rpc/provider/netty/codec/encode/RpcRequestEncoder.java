package com.frame.rpc.provider.netty.codec.encode;

import com.frame.rpc.provider.data.RpcRequest;
import com.frame.rpc.provider.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RpcRequestEncoder extends MessageToMessageEncoder<RpcRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest request, List<Object> out) throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(request);
            ByteBuf buf = ctx.alloc().buffer(bytes.length);
            buf.writeBytes(bytes);
            out.add(buf);
        } catch (Exception e) {
            log.error("RpcRequestEncoder error,msg={}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
