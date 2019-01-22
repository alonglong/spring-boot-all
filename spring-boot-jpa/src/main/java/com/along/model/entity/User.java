package com.along.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description: 用户实体类
 * @Author along
 * @Date 2019/1/8 16:50
 */
@Entity
@Table(name = "user") //对应数据库中的表名
//EntityGraph(实体图)使用演示，解决查询N+1问题
@NamedEntityGraphs({
        @NamedEntityGraph(name = "user.all",
                attributeNodes = { // attributeNodes 用来定义需要懒加载的属性
                        @NamedAttributeNode("articleList"), // 无延伸
                        @NamedAttributeNode("roles"), // 无延伸
                }
        ),
})
public class User extends BaseData {

    private static final long serialVersionUID = -5103936306962248929L;

    private String name;

    private String password;

    private Integer sex; // 1:男；0：女

    private Integer status = 1; //-1：删除；0 禁用 1启用

    private String email;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 一对多配置演示
     * 级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
     * 拥有mappedBy注解的实体类为关系被维护端
     * mappedBy="user"中的user是Article中的user属性
     */
    @JsonIgnoreProperties(value = {"user", "content"}) //解决循环引用问题，content内容大，不加载
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articleList; // 文章

    /**
     * 多对多配置演示
     */
    @JsonIgnoreProperties(value = {"users"}) //解决循环引用问题
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles; // 角色外键


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
