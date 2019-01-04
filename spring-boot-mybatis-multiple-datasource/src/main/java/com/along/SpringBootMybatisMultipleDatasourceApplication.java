package com.along;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//排除DataSource自动配置类,否则会默认自动配置,不会使用我们自定义的DataSource,并且启动报错
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan({"com.along.dao"}) // 扫描包路径
public class SpringBootMybatisMultipleDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisMultipleDatasourceApplication.class, args);
    }

}

