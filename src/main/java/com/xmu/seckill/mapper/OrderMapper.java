package com.xmu.seckill.mapper;

import com.xmu.seckill.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(Order order);
    Order getOrderById(Long id);

    List<Order> getAllOrders();
}
