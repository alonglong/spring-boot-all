package com.along;

import com.along.dao.UserDao;
import com.along.model.entity.User;
import com.along.model.vo.UserVo;
import com.along.service.UserService;
import org.assertj.core.internal.bytebuddy.matcher.StringMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.PropertyValueTransformer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJpaApplicationTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

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
                .withIgnoreCase("name");


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

    @Test
    public void findUserByNameTest() {
        final String name = "along";

        List<User> userList = userDao.findAll(new Specification<User>() {

            /*
             *
             * @param root 代表要查询的实体类型
             * @param query 用来添加查询条件，可以从中可到 Root 对象, 即告知 JPA Criteria 查询要查询哪一个实体类.
             *              还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象.
             * @param criteriaBuilder 构建 Predicate 对象，用于创建 Criteria 相关对象的工厂.
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("name"), name);
                return predicate;
            }
        });



    }

    @Test
    public void queryTest() {
//        List<User> userList = userService.findUserByNameAndSex0("along",1);
//        List<User> userList = userDao.findAll();
//        List<User> userList = userDao.findUserByNameAndSex("along", 1);
        List<User> userList = userService.findUserByArticleAndRole("5f69fb9d-c42b-4b39-abb3-109723d97b10", "692b6903-74ce-4c12-b519-470cbb255c29");
        System.out.println(userList.size());
        for (User user : userList) {
            System.out.println(user.getName());
        }
    }


}

