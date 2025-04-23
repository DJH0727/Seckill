package com.xmu.seckill.controller;

import com.xmu.seckill.common.Result;
import com.xmu.seckill.common.ResultCode;
import com.xmu.seckill.entity.User;
import com.xmu.seckill.service.SeckillService;
import com.xmu.seckill.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckill")
@Tag(name = "秒杀接口", description = "处理商品秒杀相关的请求")
public class SeckillController {


    private final SeckillService seckillService;
    private final UserService userService;

    @Autowired
    public  SeckillController(SeckillService seckillService, UserService userService) {
        this.seckillService = seckillService;
        this.userService = userService;
    }

    @Operation(summary = "发起秒杀请求", description = "根据商品 ID 和用户 ID 发起秒杀")
    @PostMapping("/{productId}")
    public Result<String> seckill(@PathVariable Long productId, @RequestParam Long userId) {
        /*
        User user = userService.findById(userId);
        if (user == null) {
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }
         */

        return seckillService.doSeckill(productId, userId);
    }
}