package com.xmu.seckill.task;

import com.xmu.seckill.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.xmu.seckill.constants.RedisKey.STOCK_DIRTY_SET;
import static com.xmu.seckill.constants.RedisKey.STOCK_KEY_PREFIX;


@Slf4j
@Component
public class StockSyncTask {
    private static final Logger logger = LoggerFactory.getLogger(StockSyncTask.class);
    private StringRedisTemplate redisTemplate;
    private ProductMapper productMapper;
    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    // 每 30 秒执行一次库存同步
    @Scheduled(fixedRate = 30000)
    public void syncStockToDB() {
        Set<String> dirtyProductIds = redisTemplate.opsForSet().members(STOCK_DIRTY_SET);
        if (dirtyProductIds == null || dirtyProductIds.isEmpty()) {
            return;
        }

        for (String productIdStr : dirtyProductIds) {
            String redisKey = STOCK_KEY_PREFIX + productIdStr;
            String stockStr = redisTemplate.opsForValue().get(redisKey);
            if (stockStr != null) {
                try {
                    int stock = Integer.parseInt(stockStr);
                    Long productId = Long.valueOf(productIdStr);
                    productMapper.updateStock(productId, stock);
                    logger.info("同步库存成功：商品ID={}，库存={}", productId, stock);
                } catch (NumberFormatException e) {
                    logger.error("库存解析失败：{}", stockStr);
                }
            }
            redisTemplate.opsForSet().remove(STOCK_DIRTY_SET, productIdStr);
        }



    }


}
