package com.xmu.seckill.service.impl;

import com.xmu.seckill.entity.Product;
import com.xmu.seckill.mapper.ProductMapper;
import com.xmu.seckill.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public void addProduct(Product product) {
        productMapper.insertProduct(product);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.findById(id);
    }

    @Override
    public List<Product> getAll() {
        return productMapper.findAll();
    }

    @Override
    public void updateStock(Long id, Integer stock) {
        productMapper.updateStock(id, stock);
    }

    @Override
    public boolean changeStock(Long id, Integer change){
        Product product = productMapper.findById(id);
        if(product == null)return false;
        int newStock = product.getStock() + change;
        if(newStock < 0)return false;
        product.setStock(newStock);
        productMapper.updateStock(id, newStock);
        return true;
    }

    @Override
    public boolean checkStock(Long id, Integer quantity) {
        Product product = productMapper.findById(id);
        return product.getStock() >= quantity;
    }
    @Override
    public Integer getStock(Long id) {
        Product product = productMapper.findById(id);
        return product.getStock();
    }
}
