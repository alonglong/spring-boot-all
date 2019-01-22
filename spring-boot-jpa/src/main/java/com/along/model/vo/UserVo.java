package com.along.model.vo;

import com.along.model.entity.Article;
import com.along.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description: 返回前端的类，vo可以控制返回的字段，也是解决循环引用的一种方案
 * @Author along
 * @Date 2019/1/10 13:29
 */
public interface UserVo {

    String getId();

    String getName();

    String getPassword();

    Integer getSex(); // 1:男；0：女

    Integer getStatus(); //-1：删除；0 禁用 1启用

    String getEmail();

    Date getBirthday();

    //@JsonIgnoreProperties(value = {"user", "content"}) //解决循环引用问题
    //List<Article> getArticleList(); // 文章列表

    //@JsonIgnoreProperties(value = {"users"}) //解决循环引用问题
    //Set<Role> getRoles(); // 角色外键
}
