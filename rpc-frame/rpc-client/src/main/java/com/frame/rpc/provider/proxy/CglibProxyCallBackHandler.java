package com.frame.rpc.provider.proxy;

import com.frame.rpc.provider.data.RpcRequest;
import com.frame.rpc.provider.data.RpcResponse;
import com.frame.rpc.provider.exception.RpcException;
import com.frame.rpc.provider.request.RpcRequestManager;
import com.frame.rpc.provider.util.RequestIdUtil;
import com.frame.rpc.provider.spring.SpringBeanFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @description
 * @author: ts
 * @create:2021-05-12 00:11
 */
public class CglibProxyCallBackHandler implements MethodInterceptor {


    /**
     * 封装rpc请求，底层执行rpc请求获取rpc结果
     * @param o
     * @param method
     * @param parameters
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        //放过toString,hashcode，equals等方法，采用spring工具类
        if ( ReflectionUtils.isObjectMethod(method)) {
            return method.invoke(method.getDeclaringClass().newInstance(),parameters);
        }
        // 1、获取rpc请求所需要的所有参数
        String requestId = RequestIdUtil.requestId();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 构建rpc 请求对象
        RpcRequest request = RpcRequest.builder()
                .requestId(requestId)
                .className(className)
                .methodName(methodName)
                .parameterTypes(parameterTypes)
                .parameters(parameters).build();

        // 真正发送rpc请求获取结果
        RpcRequestManager requestManager = SpringBeanFactory.getBean(RpcRequestManager.class);
        if (requestManager==null) {
            throw new RpcException("RpcRequestManager not existed");
        }
        RpcResponse response = requestManager.sendRequest(request);
        //返回结果数据
        return response.getResult();
    }
}
