package com.frame.rpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description
 * @author: ts
 * @create:2021-05-10 11:34
 */
@SpringBootApplication(scanBasePackages = {"com.frame.rpc.provider", "com.frame.rpc"})
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class,args);
    }
}
