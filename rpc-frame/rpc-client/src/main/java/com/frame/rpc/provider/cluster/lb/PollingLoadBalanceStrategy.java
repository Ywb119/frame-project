package com.frame.rpc.provider.cluster.lb;

import com.frame.rpc.provider.annotation.RpcLoadBalance;
import com.frame.rpc.provider.cluster.LoadBalanceStrategy;
import com.frame.rpc.provider.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@RpcLoadBalance(strategy = "polling")
@Slf4j
public class PollingLoadBalanceStrategy implements LoadBalanceStrategy {
    private int index;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        try {
            lock.tryLock(10, TimeUnit.SECONDS);
            if (index >= serviceProviders.size()) {
                index = 0;
            }
            ServiceProvider serviceProvider = serviceProviders.get(index);
            index++;
            return serviceProvider;
        } catch (InterruptedException e) {
            log.error("轮询策略获取锁失败,msg={}", e.getMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }
}
