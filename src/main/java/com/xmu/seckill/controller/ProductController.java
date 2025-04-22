package com.xmu.seckill.controller;

import com.xmu.seckill.common.Result;
import com.xmu.seckill.dto.ProductRequest;
import com.xmu.seckill.entity.Product;
import com.xmu.seckill.service.ProductService;
import com.xmu.seckill.util.Dto2Entity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品接口", description = "商品管理相关接口：添加商品、查询商品、更新库存")
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "添加商品", description = "传入商品信息，添加一条商品记录")
    @PostMapping("/add")
    public Result<String> addProduct(@RequestBody ProductRequest productRequest) {
        Product product = Dto2Entity.productDto2Product(productRequest);
        productService.addProduct(product);
        return  Result.success("商品添加成功");
    }

    @Operation(summary = "根据ID查询商品", description = "传入商品ID，查询对应商品详情")
    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @Operation(summary = "获取所有商品", description = "返回商品列表")
    @GetMapping("/list")
    public Result<List<Product>> getAllProducts() {
        return Result.success(productService.getAll());
    }

    @Operation(summary = "更新库存", description = "根据商品ID更新库存数量")
    @PostMapping("/updateStock")
    public Result<String> updateStock(@RequestParam Long id, @RequestParam Integer stock) {
        productService.updateStock(id, stock);
        return  Result.success("库存更新成功");
    }
}
