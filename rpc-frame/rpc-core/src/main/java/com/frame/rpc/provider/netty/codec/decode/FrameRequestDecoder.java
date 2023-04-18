package com.frame.rpc.provider.netty.codec.decode;

import com.frame.rpc.provider.data.RpcRequest;
import com.frame.rpc.provider.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FrameRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 二次解码器
     */
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> out) {
        try {
            int length = byteBuf.readableBytes();
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            RpcRequest request = ProtostuffUtil.deserialize(bytes, RpcRequest.class);
            out.add(request);
        } catch (Exception e) {
            log.error("RpcRequest decode error ,sg={}", e.getMessage());
        }
    }
}
