package com.xmu.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // 队列名称
    public static final String SECKILL_QUEUE = "seckill_order_queue";
    // 交换机名称
    public static final String SECKILL_EXCHANGE = "seckill_exchange";
    // 路由键名称
    public static final String SECKILL_ROUTING_KEY = "seckill_order_routing_key";

    // 声明队列
    @Bean
    public Queue queue() {
        return new Queue(SECKILL_QUEUE, true); // 队列持久化
    }

    // 声明交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(SECKILL_EXCHANGE);
    }

    // 将队列和交换机绑定
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SECKILL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 用于创建 RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
