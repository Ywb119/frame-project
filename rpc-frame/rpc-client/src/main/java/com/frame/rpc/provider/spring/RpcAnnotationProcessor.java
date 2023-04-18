package com.frame.rpc.provider.spring;

import com.frame.rpc.provider.annotation.RpcRemote;
import com.frame.rpc.provider.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
public class RpcAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {


    private ProxyFactory proxyFactory;

    /**
     * 设置代理类
     *
     * @param bean     当前controller对象
     * @param beanName bean名称
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //获取controller中的所有属性
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                //如果是私有属性需要设置公开
                field.setAccessible(true);
                //扫描当前类中包含该注解的属性
                RpcRemote rpcRemote = field.getAnnotation(RpcRemote.class);
                if (rpcRemote != null) {
                    //获取需要生成代理类的type
                    Object proxy = proxyFactory.newProxyInstance(field.getType());
                    if (proxy != null) {
                        field.set(bean, proxy);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to init remote service reference at filed " + field.getName() + " in class " + bean.getClass().getName() + ", cause: " + e.getMessage(), e);
            }
        }
        return bean;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        proxyFactory = applicationContext.getBean(ProxyFactory.class);
    }
}
