package com.xmu.seckill.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.xmu.seckill.mapper.ProductMapper;
import com.xmu.seckill.entity.Product;

import java.util.List;

import  com.xmu.seckill.constants.RedisKey;

/**
 * 项目启动时，把商品库存预热到 Redis
 */
@Component
public class StockPreloadRunner implements ApplicationRunner {
    private static final  Logger logger = LoggerFactory.getLogger(StockPreloadRunner.class);


    private final ProductMapper productMapper;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public StockPreloadRunner(ProductMapper productMapper, StringRedisTemplate redisTemplate) {
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
    }



    @Override
    public void run(ApplicationArguments args) {
        //从数据库中查询出所有商品
        List<Product> productList = productMapper.findAll(); // 查询所有商品

        //  遍历商品列表，将每个商品的库存信息写入 Redis
        for (Product product : productList) {
            // Redis 中的 key 形如：seckill:stock:1
            String key = RedisKey.STOCK_KEY_PREFIX + product.getId();
            // 写入库存数量到 Redis（注意是字符串类型）
            redisTemplate.opsForValue().set(key, String.valueOf(product.getStock()));
            logger.info("商品 [{}] 的库存 [{}] 已成功写入 Redis！", product.getName(), product.getStock());
        }

        logger.info("Redis 库存预热完成！");

    }
}
