package com.along.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2023/12/15 10:04
 */
@Configuration
public class RabbitMqConfig {

    // 定义交换机的名字
    private static final String EXCHANGE_NAME = "boot_topic_exchange";
    // 定义队列名
    private static final String QUEUE_NAME = "boot_queue";

    // 1.声明交换机
    @Bean
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME)
                .durable(true) // 是否持久化
                .build();
    }

    // 2.声明队列
    @Bean
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    // 3.队列与交换机绑定
    @Bean
    public Binding bindQueueExchange(@Qualifier("bootExchange") Exchange exchange, @Qualifier("bootQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }
}
