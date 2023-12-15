package com.along.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 简单模式配置
 * @Author along
 * @Date 2023/12/15 15:53
 */
@Configuration
public class HelloWordConfig {

    public Queue setQueue() {
        return new Queue("helloWord_queue");
    }
}
