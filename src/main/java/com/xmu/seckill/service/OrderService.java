package com.xmu.seckill.service;

import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);
    List<Order> getAllOrders();
}
