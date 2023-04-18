package com.frame.rpc.provider.proxy;

/**
 * @description
 * @author: ts
 * @create:2021-05-11 16:48
 */
public interface ProxyFactory {

    <T> T newProxyInstance(Class<T> cls);
}
