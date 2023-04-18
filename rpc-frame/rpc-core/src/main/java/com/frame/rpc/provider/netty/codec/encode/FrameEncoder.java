package com.frame.rpc.provider.netty.codec.encode;

import io.netty.handler.codec.LengthFieldPrepender;

public class FrameEncoder extends LengthFieldPrepender {

    public FrameEncoder() {
        super(4);
    }
}
