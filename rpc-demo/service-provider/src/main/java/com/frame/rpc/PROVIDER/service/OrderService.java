package com.frame.rpc.provider.service;


import com.frame.rpc.provider.annotation.RpcService;
import com.frame.rpc.server.config.RpcServerConfiguration;

import javax.annotation.Resource;

@RpcService(interfaceClass = OrderService.class)
public class OrderService {

    @Resource
    private RpcServerConfiguration serverConfiguration;

    public String getOrder(String userId, String orderNo) {
        return serverConfiguration.getServerPort() +"---"+serverConfiguration.getRpcPort()+"---Congratulations, The RPC call succeeded,orderNo is "+orderNo +",userId is " +userId;
    }
}
