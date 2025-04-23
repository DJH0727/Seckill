package com.xmu.seckill.service;

import com.xmu.seckill.config.RabbitMQConfig;
import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.dto.SeckillMessage;
import com.xmu.seckill.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import  com.xmu.seckill.constants.RedisKey;

@Service
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);


    private final OrderService orderService;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public MessageConsumer(OrderService orderService, StringRedisTemplate redisTemplate) {
        this.orderService = orderService;
        this.redisTemplate = redisTemplate;
    }

    // 消息队列监听器，接收到订单创建消息后处理订单
    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    public void processOrderMessage(SeckillMessage message) {
        Long userId = message.getUserId();
        Long productId = message.getProductId();

        // 创建订单
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setGoodsId(productId);
        orderRequest.setQuantity(1);

        //减库存,先查后改，在高并发场景下存在并发问题
        //int stock = productService.getStock(productId);
        //productService.updateStock(productId, stock - 1);

        Order order = orderService.createOrder(orderRequest);
        //标记 productId 对应的库存已被修改，用于同步库存到数据库
        redisTemplate.opsForSet().add(RedisKey.STOCK_DIRTY_SET, productId.toString());

        logger.info("订单创建成功，订单信息：{}",  order.toString());
    }
}
