package com.xmu.seckill.controller;

import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.entity.Order;
import com.xmu.seckill.service.OrderService;
import com.xmu.seckill.common.Result;
import com.xmu.seckill.service.ProductService;
import com.xmu.seckill.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * OrderController
 * @date 2025/4/21
 */
@Tag(name = "订单接口", description = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    @Autowired
    public OrderController(OrderService orderService, ProductService productService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }


    /**
     * @param request CreateOrderRequest
     * @return Result<String>
     */
    @Operation(summary = "创建订单", description = "用户提交订单请求，后台验证用户、商品、库存后创建订单")
    @PostMapping("/create")
    public Result<String> createOrder(@RequestBody CreateOrderRequest request) {
        logger.info("createOrder request: {}", request.toString());
        // 检查用户是否存在
        if(userService.findById(request.getUserId()) == null) {
            return Result.failed("用户不存在:"+request.getUserId().toString());
        }
        // 检查商品是否存在
        if(productService.getById(request.getGoodsId())==null){
            return Result.failed("商品不存在:"+request.getGoodsId().toString());
        }
        if(request.getQuantity()<=0) {
            return Result.failed("商品数量必须大于0");
        }
        try {

            // 检查库存
            int stock = productService.getStock(request.getGoodsId());
            if (stock - request.getQuantity() < 0)
                return Result.failed("库存不足");
            //减库存
            productService.updateStock(request.getGoodsId(), stock - request.getQuantity());
            // 调用 OrderService 创建订单
            Order order = orderService.createOrder(request);
            if(order == null){
                //回滚库存
                productService.updateStock(request.getGoodsId(), stock);
                return Result.failed("订单创建失败");
            }

            return Result.success("订单创建成功，：" + order);
        } catch (RuntimeException e) {
            return Result.failed(e.getMessage());
        }
    }
}
