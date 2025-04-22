package com.xmu.seckill.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Long goodsId;
    private Integer quantity;
}
