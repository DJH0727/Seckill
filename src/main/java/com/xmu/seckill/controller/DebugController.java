package com.xmu.seckill.controller;

import com.xmu.seckill.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Set;


@RestController
@RequestMapping("/debug")
@Tag(name = "调试接口", description = "用于开发期间调试相关功能")
public class DebugController {

    private static final String SECKILL_KEY_PREFIX = "seckill:";
    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    private final StringRedisTemplate redisTemplate;
    @Autowired
    public DebugController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/resetRedis")
    @Operation(summary = "重置 Redis 数据", description = "删除 Redis 中所有 seckill: 开头的 key")
    public Result<String> resetRedis() {
        //
        var keys = redisTemplate.keys(SECKILL_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            logger.info("清除SecKill相关数据成功，共清除 {} 条记录", keys.size());
            return Result.success(" 清除SecKill相关数据成功，共清除 " + keys.size() + " 条记录");

        }
        return Result.success("Redis 为空");
    }
}
