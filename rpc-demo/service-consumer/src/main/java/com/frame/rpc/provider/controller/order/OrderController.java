package com.frame.rpc.provider.controller.order;

import com.frame.rpc.provider.annotation.RpcRemote;
import com.frame.rpc.provider.order.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: ts
 * @create:2021-05-10 11:30
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @RpcRemote
    private OrderService orderService;

    /**
     * 获取订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    @GetMapping("/getOrder")
    public String getOrder(String userId,String orderNo) {
        return orderService.getOrder(userId,orderNo);
    }

}
