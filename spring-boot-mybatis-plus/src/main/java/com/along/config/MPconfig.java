package com.along.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Description: mybatis-plus配置
 * @Author along
 * @Date 2020/4/6 0:10
 */
@Configuration
public class MPconfig {

    // 注入人家写好的插件处理器
    // 本质是一个拦截器 Interceptor
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    // 分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    // 逻辑删除插件！
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    // SQL执行效率插件
    @Bean
    @Profile({"dev","test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // 允许执行的sql的最长时间 ， 默认的单位是ms
        performanceInterceptor.setMaxTime(1000);
        performanceInterceptor.setFormat(true); // 格式化SQL代码
        return performanceInterceptor;
    }

}
