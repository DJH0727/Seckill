package com.xmu.seckill.service;

import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.entity.Order;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);
}
