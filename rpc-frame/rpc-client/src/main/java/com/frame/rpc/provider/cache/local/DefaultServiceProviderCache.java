package com.frame.rpc.provider.cache.local;

import com.frame.rpc.provider.cache.ServiceProviderCache;
import com.frame.rpc.provider.provider.ServiceProvider;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @description
 * @author: ts
 * @create:2021-05-11 15:28
 */
@Component
public class DefaultServiceProviderCache implements ServiceProviderCache {

    @Resource
    private LoadingCache<String, List<ServiceProvider>> cache;

    @Override
    public void put(String key, List<ServiceProvider> value) {
        cache.put(key,value);
    }

    @Override
    public List<ServiceProvider> get(String key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            return Lists.newArrayListWithCapacity(0);
        }
    }


    @Override
    public void evict(String key) {
        cache.invalidate(key);
    }

    @Override
    public void update(String key, List<ServiceProvider> value) {
        evict(key);
        put(key,value);
    }
}
