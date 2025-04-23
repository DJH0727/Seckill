package com.xmu.seckill.service;

import com.xmu.seckill.config.RabbitMQConfig;
import com.xmu.seckill.dto.CreateOrderRequest;
import com.xmu.seckill.dto.SeckillMessage;
import com.xmu.seckill.entity.Order;
import com.xmu.seckill.mapper.OrderMapper;
import com.xmu.seckill.mapper.ProductMapper;
import com.xmu.seckill.util.Dto2Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);


    private final OrderService orderService;
    private final ProductService productService;
    @Autowired
    public MessageConsumer(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    // 消息队列监听器，接收到订单创建消息后处理订单
    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    public void processOrderMessage(SeckillMessage message) {
        Long userId = message.getUserId();
        Long productId = message.getProductId();

        // 模拟订单创建逻辑（真实应用中应处理事务和其他边界情况）
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setGoodsId(productId);
        orderRequest.setQuantity(1);

        //减库存,先查后改，在高并发场景下存在并发问题
        //int stock = productService.getStock(productId);
        //productService.updateStock(productId, stock - 1);

        //Order order = orderService.createOrder(orderRequest);

        logger.info("订单创建成功，订单信息：{}",  "暂未实现");
    }
}
