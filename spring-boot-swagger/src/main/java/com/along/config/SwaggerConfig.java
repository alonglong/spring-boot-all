package com.along.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: swagger配置类
 * @Author along
 * @Date 2020/1/11 19:58
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 非管理原接口
     * @return
     */
    @Bean
    public Docket webApiConfig(Environment environment) {
        //设置要显示swagger的环境
        Profiles profiles = Profiles.of("dev","test");
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(flag)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo webApiInfo(){
        // 建造者模式
        return new ApiInfoBuilder()
                .title("Swagger API文档1")
                .description("Swagger文档")
                .version("1.0")
                .contact(new Contact("along", "https://github.com/alonglong", "jin.along@qq.com"))
                .build();
    }

    /**
     * 后台管理员接口
     * @return
     */
    @Bean
    public Docket adminApiConfig(Environment environment){
        //设置要显示swagger的环境
        Profiles profiles = Profiles.of("dev","test");
        boolean flag = environment.acceptsProfiles(profiles);

        // 只要 /admin 的所有请求
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(flag)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    private ApiInfo adminApiInfo(){
        // 建造者模式
        return new ApiInfoBuilder()
                .title("Swagger API文档2")
                .description("Swagger文档")
                .version("1.0")
                .contact(new Contact("along", "https://github.com/alonglong", "jin.along@qq.com"))
                .build();
    }
}
