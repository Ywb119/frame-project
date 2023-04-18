package com.frame.rpc.provider.boot;


import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 22:13
 */
@Configuration
public class RpcBootstrap {

    @Resource
    private RpcClientRunner clientRunner;

    @PostConstruct
    public void initRpcClient() {
        clientRunner.run();
    }
}
