package com.xmu.seckill.mapper;

import com.xmu.seckill.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void insertOrder(Order order);
    Order getOrderById(Long id);
}
