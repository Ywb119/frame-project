package com.frame.rpc.provider.netty.handler;

import com.frame.rpc.provider.data.RpcRequest;
import com.frame.rpc.provider.data.RpcResponse;
import com.frame.rpc.provider.spring.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 21:52
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        log.info("rpc 服务端收到的请求是:{}", request);
        // 构造响应对象
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            //获取请求的相关数据
            //接口名称
            String className = request.getClassName();
            //方法名称
            String methodName = request.getMethodName();
            //获取类型
            Class<?>[] parameterTypes = request.getParameterTypes();
            //获取参数
            Object[] parameters = request.getParameters();

            Object bean = SpringBeanFactory.getBean(Class.forName(className));
            Method method = bean.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(bean, parameters);
            response.setResult(result);
        } catch (Throwable e) {
            response.setCause(e);
            log.error("rpc server invoke error,msg={}", e.getMessage());
        } finally {
            log.info("服务端执行成功,响应为:{}", response);
            ctx.channel().writeAndFlush(response);
        }
    }
}
