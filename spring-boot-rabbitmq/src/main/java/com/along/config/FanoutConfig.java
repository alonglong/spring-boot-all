package com.along.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 发布订阅模式配置
 * @Author along
 * @Date 2023/12/15 15:55
 */
@Configuration
public class FanoutConfig {

    // 声明队列
    @Bean
    public Queue fanoutQ1() {
        return new Queue("fanout.q1");
    }
    @Bean
    public Queue fanoutQ2() {
        return new Queue("fanout.q2");
    }

    // 声明exchange
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    // 声明binding,exchange与queue绑定
    @Bean
    public Binding bindQ1(@Qualifier("fanoutQ1") Queue fanoutQ1, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQ1).to(fanoutExchange);
    }
    @Bean
    public Binding bindQ2(@Qualifier("fanoutQ2") Queue fanoutQ2, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQ2).to(fanoutExchange);
    }


}
