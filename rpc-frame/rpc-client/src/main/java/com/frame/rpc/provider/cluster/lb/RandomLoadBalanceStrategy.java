package com.frame.rpc.provider.cluster.lb;

import com.frame.rpc.provider.cluster.LoadBalanceStrategy;
import com.frame.rpc.provider.provider.ServiceProvider;
import com.frame.rpc.provider.annotation.RpcLoadBalance;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

@RpcLoadBalance(strategy = "random")
public class RandomLoadBalanceStrategy implements LoadBalanceStrategy {
    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        /**
         * [0,len)
         */
        int len = serviceProviders.size();
        int index = RandomUtils.nextInt(0,len);
        return serviceProviders.get(index);
    }
}
