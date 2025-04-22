package com.xmu.seckill.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Long id;
    private String name;
    private Integer stock;
    private Double price;
}
