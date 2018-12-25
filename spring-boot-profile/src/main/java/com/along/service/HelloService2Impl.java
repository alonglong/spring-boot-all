package com.along.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2018/12/25 18:09
 */
@Service
@Profile("service2")
public class HelloService2Impl implements HelloService {

    @Override
    public String hello() {
        return "service 2";
    }
}
