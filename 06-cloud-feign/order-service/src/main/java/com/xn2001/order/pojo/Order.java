package com.xn2001.order.pojo;

import com.xn2001.feign.pojo.User;
import lombok.Data;

@Data
public class Order {
    private Long id;
    private Long price;
    private String name;
    private Integer num;
    private Long userId;
    private User user;
}