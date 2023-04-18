package com.frame.rpc.provider.cluster;


import com.frame.rpc.provider.annotation.RpcLoadBalance;
import com.frame.rpc.provider.config.RpcClientConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultStartegyProvider implements StartegyProvider, ApplicationContextAware {

    @Autowired
    private RpcClientConfiguration clientConfiguration;

    private LoadBalanceStrategy strategy;

    @Override
    public LoadBalanceStrategy getStrategy() {
        return strategy;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(RpcLoadBalance.class);
        for(Object bean:map.values()) {
            RpcLoadBalance hrpcLoadBalance = bean.getClass().getAnnotation(RpcLoadBalance.class);
            if (hrpcLoadBalance.strategy().equals(clientConfiguration.getRpcClientClusterStrategy()==null?"random":clientConfiguration.getRpcClientClusterStrategy())) {
                strategy = (LoadBalanceStrategy) bean;
                break;
            }
        }
    }
}
