package com.frame.rpc.server.boot;


import com.frame.rpc.server.registry.RpcRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RpcServerRunner {

    @Resource
    private RpcRegistry registry;

    @Resource
    private RpcServer rpcServer;

    /**
     * 启动 rpc server
     */
    public void run() {
        /*
         * 1、服务注册
         * 2、基于netty绑定端口，启动服务，监听连接，接收连接处理数据
         */
        registry.serviceRegistry();
        rpcServer.start();
    }
}
