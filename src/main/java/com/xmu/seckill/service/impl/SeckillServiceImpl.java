package com.xmu.seckill.service.impl;


import com.xmu.seckill.common.Result;

import com.xmu.seckill.service.MessageProducer;
import com.xmu.seckill.service.SeckillService;
import com.xmu.seckill.constants.RedisKey;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;



import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Service
public class SeckillServiceImpl implements SeckillService {
    private static final Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    private final StringRedisTemplate redisTemplate;
    private final MessageProducer messageProducer;

    private DefaultRedisScript<Long> seckillScript;
    @Autowired
    public SeckillServiceImpl(StringRedisTemplate redisTemplate, MessageProducer messageProducer) {
        this.redisTemplate = redisTemplate;
        this.messageProducer = messageProducer;
    }
    @PostConstruct
    public void initLuaScript() {
        // 初始化 Lua 脚本 ,保证操作原子性
        seckillScript = new DefaultRedisScript<>();
        seckillScript.setLocation(new ClassPathResource("lua/seckill.lua"));
        seckillScript.setResultType(Long.class);
        logger.info("init lua script success");
    }

    @Override
    public Result<String> doSeckill(Long productId, Long userId) {
        String stockKey = RedisKey.STOCK_KEY_PREFIX + productId;
        String userKey = RedisKey.RESULT_KEY_PREFIX + productId + ":" + userId;
        List<String> keys = Arrays.asList(stockKey, userKey);
        Long result = redisTemplate.execute(seckillScript, keys);
        if (result == null) {
            logger.error("Lua 脚本执行失败");
            return Result.failed("秒杀异常，请稍后重试");
        }
        if (result == 0L) {
            //logger.info("用户 {} 重复秒杀商品 {}", userId, productId);
            return Result.failed("不能重复秒杀");
        } else if (result == -1L) {
            //logger.info("库存不足，用户 {} 秒杀商品 {} 失败", userId, productId);
            return Result.failed("库存不足");
        }
        else if (result == -2L) {
           // logger.info("Redis未缓存，用户 {} 秒杀商品 {} 失败", userId, productId);
            return Result.failed("秒杀失败：库存未初始化");
        }

        // 库存减1，用户参与标记
        //redisTemplate.opsForValue().set(userKey, "1", 1, TimeUnit.DAYS);

        // 发送消息到队列，由异步消费者处理订单创建
        messageProducer.sendOrderCreateMessage(userId, productId);


        logger.info("stock is success, userId: {} productId: {}",userId , productId );

        return Result.success("秒杀成功, 订单创建中");
    }
}
