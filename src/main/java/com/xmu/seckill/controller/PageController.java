package com.xmu.seckill.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PageController", description = "页面相关接口")
@Controller
public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(PageController.class);
    @Operation(summary = "显示登录页面", description = "显示登录页面")
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // 显示 login.html 页面
    }

    @Operation(summary = "显示注册页面", description = "显示注册页面")
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // 指向 register.html
    }

    @Operation(summary = "商品列表页面", description = "显示商品列表页面")
    @GetMapping("/products")
    public String showProductsPage() {
        return "products";  // 显示 products.html 页面
    }

}
