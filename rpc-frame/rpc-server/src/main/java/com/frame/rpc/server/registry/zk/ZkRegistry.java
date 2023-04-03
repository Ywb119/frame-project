package com.frame.rpc.server.registry.zk;

import com.frame.rpc.provider.annotation.RpcService;
import com.frame.rpc.server.config.RpcServerConfiguration;
import com.frame.rpc.server.registry.RpcRegistry;
import com.frame.rpc.provider.spring.SpringBeanFactory;
import com.frame.rpc.provider.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 20:40
 */
@Component
@Slf4j
public class ZkRegistry implements RpcRegistry {


    @Resource
    private ServerZKit zKit;

    @Resource
    private RpcServerConfiguration rpcServerConfiguration;


    /**
     * 真正完成服务注册
     */
    @Override
    public void serviceRegistry() {
        // 找到所有标注@HrpcService注解的 bean，将服务信息写入到zk中
        Map<String, Object> beanListByAnnotationClass = SpringBeanFactory.getBeanListByAnnotationClass(RpcService.class);
        if (beanListByAnnotationClass.size() > 0) {
            // 在zk中创建根节点
            zKit.createRootNode();
            String ip = IpUtil.getRealIp();
            for (Object bean : beanListByAnnotationClass.values()) {
                // 获取bean上的@RpcService
                RpcService hrpcService = bean.getClass().getAnnotation(RpcService.class);
                // 获取注解上的 interfaceClass 属性 获取接口信息
                Class<?> interfaceClass = hrpcService.interfaceClass();
                // 创建接口目录
                String serviceName = interfaceClass.getName();
                zKit.createPersistentNode(serviceName);
                // 在该接口下创建临时节点信息 ip:port
                String node = ip + ":" + rpcServerConfiguration.getRpcPort();
                zKit.createNode(serviceName + "/" + node);
                log.info("服务{}-{}注册成功", serviceName, node);
            }
        }
    }
}
