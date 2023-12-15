package com.along.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 工作队列模式配置
 * @Author along
 * @Date 2023/12/15 16:00
 */
@Configuration
public class WorkConfig {

    @Bean
    public Queue workQ1() {
        return new Queue("work.q1");
    }
}

