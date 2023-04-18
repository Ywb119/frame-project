package com.frame.rpc.provider.boot;

import com.frame.rpc.provider.proxy.discovery.RpcServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 22:13
 */
@Slf4j
@Component
public class RpcClientRunner {

    @Resource
    private RpcServiceDiscovery serviceDiscovery;

    public void run() {
        /**
         * 1、服务发现（从zk中获取服务信息）
         * 2、客户端controller中如果有@HrpcRemote注解，需要创建代理对象并注入
         */
        serviceDiscovery.serviceDiscovery();

    }
}
