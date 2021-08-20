package com.xn2001.order.service;

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
    //@Autowired
    //private RestTemplate restTemplate;
    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2. 返回
        return order;
    }
    public Order queryOrderAndUserById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // TODO: 2021/8/20 使用feign远程调用
        User user = userClient.findById(order.getUserId());
        // 3. 将用户信息封装进订单
        order.setUser(user);
        // 4.返回
        return order;
    }
}
