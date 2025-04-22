package com.xmu.seckill.util;

import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.dto.ProductRequest;
import com.xmu.seckill.dto.UserRequest;
import com.xmu.seckill.entity.Order;
import com.xmu.seckill.entity.Product;
import com.xmu.seckill.entity.User;

public class Dto2Entity {
    public static User UserDto2Entity(UserRequest dto) {

        return User.builder()
                .name(dto.getName())
                .password(dto.getPassword())
                .build();
    }

    public static Product productDto2Product(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .stock(productRequest.getStock())
                .price(productRequest.getPrice())
                .build();
    }

    public static Order OrderDto2Order(CreateOrderRequest request) {
        return Order.builder()
                .userId(request.getUserId())
                .goodsId(request.getGoodsId())
                .quantity(request.getQuantity())
                .build();

    }
}
