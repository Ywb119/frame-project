package com.frame.rpc.server.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ServerZkClientConfig {

    /**
     * RPC服务端配置
     */
    @Resource
    private RpcServerConfiguration rpcServerConfiguration;

    /**
     * 声音ZK客户端
     * @return
     */
    @Bean
    public ZkClient zkClient() {
        return new ZkClient(rpcServerConfiguration.getZkAddr(), rpcServerConfiguration.getConnectTimeout());
    }
}
