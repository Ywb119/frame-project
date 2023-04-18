package com.frame.rpc.provider.cluster.lb;

import com.frame.rpc.provider.cluster.LoadBalanceStrategy;
import com.frame.rpc.provider.provider.ServiceProvider;
import com.frame.rpc.provider.annotation.RpcLoadBalance;
import com.frame.rpc.provider.util.IpUtil;

import java.util.List;

@RpcLoadBalance(strategy = "hash")
public class HashLoadBalanceStrategy implements LoadBalanceStrategy {
    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        /**
         * 1、获取客户端ip
         * 2、获取ip hash
         * 3、index = hash % serviceProviders.size()
         * 4、get(index)
         */
        String ip = IpUtil.getRealIp();
        int hashCode = ip.hashCode();
        int index = Math.abs(hashCode % serviceProviders.size());
        return serviceProviders.get(index);
    }
}
