package com.xmu.seckill.controller;

import com.xmu.seckill.common.Result;
import com.xmu.seckill.constants.RedisKey;
import com.xmu.seckill.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/debug")
@Tag(name = "调试接口", description = "用于开发期间调试相关功能")
public class DebugController {

    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    private final StringRedisTemplate redisTemplate;
    private final ProductMapper productMapper;

    @Autowired
    public DebugController(StringRedisTemplate redisTemplate, ProductMapper productMapper) {
        this.redisTemplate = redisTemplate;
        this.productMapper = productMapper;
    }

    @PostMapping("/resetRedis")
    @Operation(summary = "重置 Redis 数据", description = "删除redis中所有秒杀结果，并重置所有商品库存数量为10")
    public Result<String> resetRedis() {
        //
        var keys = redisTemplate.keys(RedisKey.RESULT_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            logger.info("清除SecKill相关数据成功，共清除 {} 条记录", keys.size());
            return Result.success(" 清除SecKill相关数据成功，共清除 " + keys.size() + " 条记录");
        }
        //重置库存数量
        redisTemplate.delete(RedisKey.STOCK_DIRTY_SET);

        var stockKeys = redisTemplate.keys(RedisKey.STOCK_KEY_PREFIX + "*");
        if (stockKeys != null && !stockKeys.isEmpty()) {
            for (var key : stockKeys) {
                redisTemplate.opsForValue().set(key, "10");
                productMapper.updateStock(Long.parseLong(key.split(":")[2]), 10);
            }
            logger.info("重置库存数量成功，共重置 {} 条记录", stockKeys.size());
        }

        return Result.success("重置成功");
    }
}
