package com.frame.rpc.provider.proxy.discovery.zk;

import com.frame.rpc.provider.cache.ServiceProviderCache;
import com.frame.rpc.provider.provider.ServiceProvider;
import com.frame.rpc.provider.proxy.discovery.RpcServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 22:17
 */
@Component
@Slf4j
public class ZkServiceDiscovery implements RpcServiceDiscovery {


    @Resource
    private ClientZKit zKit;

    @Resource
    private ServiceProviderCache serviceProviderCache;


    public void serviceDiscovery() {
        // 拉取所有服务列表
        List<String> allService = zKit.getServiceList();
        if (!allService.isEmpty()) {
            for (String serviceName : allService) {
                // 获取该接口下的所有节点(所有的提供者信息)
                List<ServiceProvider> providers = zKit.getServiceInfos(serviceName);
                //缓存该服务提供者信息
                log.info("订阅到的服务名称={},服务提供者有={}",serviceName,providers);
                serviceProviderCache.put(serviceName, providers);
                // 监听该接口下的所有节点的变化
                zKit.subscribeZKEvent(serviceName);
            }
        }

    }
}
