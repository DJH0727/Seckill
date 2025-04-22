package com.xmu.seckill.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Integer stock;
    private Double price;
}
