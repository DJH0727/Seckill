package com.xmu.seckill.service;

import com.xmu.seckill.entity.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);

    Product getById(Long id);

    List<Product> getAll();

    void updateStock(Long id, Integer stock);
    boolean changeStock(Long id, Integer change);

    boolean checkStock(Long id, Integer quantity);
    Integer getStock(Long id);

}
