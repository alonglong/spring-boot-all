package com.along;

import com.along.dao.UserDao;
import com.along.model.entity.User;
import org.assertj.core.internal.bytebuddy.matcher.StringMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.PropertyValueTransformer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJpaApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void test() {
        // 创建查询条件数据对象
        User user = new User();
        user.setName("along");
        user.setSex(1);

        // 创建匹配器，即规定如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                //方式一：单独设置name字段为模糊查询方式
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
                //方式二：设置name字段为模糊查询，忽略大小写
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING, true))
                //方式三（推荐）：链式设置
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("status", "sex")
                .withIgnoreCase()
                .withIgnoreCase(true)
                .withIgnoreCase("name")
                ;


        // 创建实例
//        Example<User> example = Example.of(user, matcher);
        Example<User> example = Example.of(user);
        // 查询
        List<User> users = userDao.findAll(example);

        //输出结果
        System.out.println("数量：" + users.size());
        for (User u : users) {
            System.out.println(u.getName());
        }

    }

}

