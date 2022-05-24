package com.xn2001.order.service;

import com.xn2001.order.entity.Order;

public interface OrderService {

    /**
     * 创建订单
     */
    Long create(Order order);
}