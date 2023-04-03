package com.frame.rpc.server.boot;


import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class RpcServerApplication {

    @Resource
    private RpcServerRunner serverRunner;

    @PostConstruct
    public void initRpcServer(){
        serverRunner.run();
    }

}
