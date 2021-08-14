package com.xn2001.order.service;

import com.xn2001.order.mapper.OrderMapper;
import com.xn2001.order.pojo.Order;
import com.xn2001.order.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 4.返回
        return order;
    }
    public Order queryOrderAndUserById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // TODO: 2021/8/15  2.查询用户，使用服务拉取
        User user = restTemplate.getForObject("http://userservice/user/" + order.getUserId(), User.class);
        // 3. 将用户信息封装进订单
        order.setUser(user);
        // 4.返回
        return order;
    }
}
