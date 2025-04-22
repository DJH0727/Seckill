package com.xmu.seckill.service.impl;

import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.entity.Order;
import com.xmu.seckill.mapper.OrderMapper;
import com.xmu.seckill.service.OrderService;
import com.xmu.seckill.util.Dto2Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    private final OrderMapper orderMapper;
    @Autowired
    public  OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Order createOrder(CreateOrderRequest request) {
        logger.info("createOrder request: {}", request);
        // 1. 创建订单对象
        Order order = Dto2Entity.OrderDto2Order(request);
        order.setCreateTime(Timestamp.from(Instant.now()).toLocalDateTime());

        // 2. 插入订单到数据库
        orderMapper.insertOrder(order);

        return order;
    }
}
