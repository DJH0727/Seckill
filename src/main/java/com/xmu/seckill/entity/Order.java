package com.xmu.seckill.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Order {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Integer quantity;
    private LocalDateTime createTime;

}
