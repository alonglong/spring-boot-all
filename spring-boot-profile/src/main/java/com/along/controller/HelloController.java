package com.along.controller;

import com.along.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2018/12/25 18:13
 */
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("hello")
    public String sayHello(){
        return helloService.hello();
    }
}
