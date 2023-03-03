package com.frame.netty.buff;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;

import java.nio.charset.StandardCharsets;

public class Buff {

    public static void main(String[] args) {
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);

        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
    }
}
