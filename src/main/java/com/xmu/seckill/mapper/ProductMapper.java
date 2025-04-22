package com.xmu.seckill.mapper;

import com.xmu.seckill.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    //@Insert("insert into product(name, stock, price) values(#{name}, #{stock}, #{price})")
    void insertProduct(Product product);

    //@Select("select * from product where id = #{id}")
    Product findById(Long id);

    //@Select("select * from product")
    List<Product> findAll();

    //@Update("update product set stock = #{stock} where id = #{id}")
    void updateStock(Long id, Integer stock);
}
