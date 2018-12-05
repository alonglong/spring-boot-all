package com.along.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2018/12/5 17:32
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello() {
        return "hello spring boot";
    }
}
