package com.along;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
@MapperScan({"com.along.dao"}) // 扫描包路径
public class SpringBootPoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootPoiApplication.class, args);
    }

}

