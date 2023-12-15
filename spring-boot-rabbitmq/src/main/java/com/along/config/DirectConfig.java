package com.along.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 路由模式配置
 * @Author along
 * @Date 2023/12/15 16:14
 */
@Configuration
public class DirectConfig {
    @Bean
    public Queue directQ1() {
        return new Queue("direct.q1");
    }
    @Bean
    public Queue directQ2() {
        return new Queue("direct.q2");
    }

    // 声明exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    // 声明binding,exchange与queue绑定
    @Bean
    public Binding bindDirectBindQ1(@Qualifier("directQ1") Queue directQ1, @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(directQ1).to(directExchange).with("directQ1.aaa");
    }
    @Bean
    public Binding bindDirectBindQ2(@Qualifier("directQ2") Queue directQ2, @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(directQ2).to(directExchange).with("directQ2.bbb");
    }
}
