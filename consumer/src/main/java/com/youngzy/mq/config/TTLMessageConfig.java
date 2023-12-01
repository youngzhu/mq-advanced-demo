package com.youngzy.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTLMessageConfig {

    @Bean
    public DirectExchange ttlDirectExchange(){
        return new DirectExchange("ttl.direct");
    }

    @Bean
    public Queue ttlQueue(){
        return QueueBuilder
                .durable("ttl.queue")
                .ttl(10000) // 设置队列的超时时间，10秒
                .deadLetterExchange("dl.direct") // 指定死信交换机
                .deadLetterRoutingKey("dl")
                .build();
    }

    @Bean
    public Binding ttlBinding(){
        return BindingBuilder.bind(ttlQueue()).to(ttlDirectExchange()).with("ttl");
    }
}
