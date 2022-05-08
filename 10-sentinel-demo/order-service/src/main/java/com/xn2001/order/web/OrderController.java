package com.xn2001.order.web;

import com.xn2001.order.pojo.Order;
import com.xn2001.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

   @Autowired
   private OrderService orderService;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        // 根据id查询订单并返回
        return orderService.queryOrderById(orderId);
    }

    //@GetMapping("/query")
    //public String queryOrder(){
    //    return "查询订单成功";
    //}
    //
    //@GetMapping("/update")
    //public String updateOrder(){
    //    return "更新订单成功";
    //}

    @GetMapping("/query")
    public String queryOrder() {
        // 查询商品
        orderService.queryGoods();
        // 查询订单
        System.out.println("查询订单");
        return "查询订单成功";
    }

    @GetMapping("/save")
    public String saveOrder() {
        // 查询商品
        orderService.queryGoods();
        // 查询订单
        System.out.println("新增订单");
        return "新增订单成功";
    }

}
