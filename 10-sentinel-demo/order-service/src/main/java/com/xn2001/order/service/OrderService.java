package com.xn2001.order.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.xn2001.feign.clients.UserClient;
import com.xn2001.feign.pojo.User;
import com.xn2001.order.mapper.OrderMapper;
import com.xn2001.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.用Feign远程调用
        User user = userClient.findById(order.getUserId());
        // 3.封装user到Order
        order.setUser(user);
        // 4.返回
        return order;
    }

    @SentinelResource("goods")
    public void queryGoods(){
        System.err.println("查询商品");
    }

}
