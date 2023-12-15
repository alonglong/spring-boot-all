package com.along.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Topic模式配置
 * @Author along
 * @Date 2023/12/15 16:04
 */
@Configuration
public class TopicConfig {
    @Bean
    public Queue topicQ1() {
        return new Queue("topic.q1");
    }
    @Bean
    public Queue topicQ2() {
        return new Queue("topic.q2");
    }

    // 声明exchange
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    // 声明binding,exchange与queue绑定
    @Bean
    public Binding bindTopicBindQ1(@Qualifier("topicQ1") Queue topicQ1, @Qualifier("topicExchange") TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQ1).to(topicExchange).with("topicQ1.#");
    }
    @Bean
    public Binding bindTopicBindQ2(@Qualifier("topicQ2") Queue topicQ2, @Qualifier("topicExchange") TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQ2).to(topicExchange).with("topicQ2.#");
    }
}
