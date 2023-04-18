package com.frame.rpc.provider.cluster;

import com.frame.rpc.provider.provider.ServiceProvider;

import java.util.List;

/**
 * @description
 * @author: ts
 * @create:2021-05-13 11:46
 */
public interface LoadBalanceStrategy {

    /**
     * 根据对应的策略进行负载均衡
     * @param serviceProviders
     * @return
     */
    ServiceProvider select(List<ServiceProvider> serviceProviders);

}
