package com.frame.rpc.provider.util;

public class RequestIdUtil {

    public static String requestId() {
//        UUID.randomUUID().toString();
        return GlobalIDGenerator.getInstance().nextStrId();
    }
}
