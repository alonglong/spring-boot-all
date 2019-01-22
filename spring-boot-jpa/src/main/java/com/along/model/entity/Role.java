package com.along.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

/**
 * @Description: 角色实体类
 * @Author along
 * @Date 2019/1/8 16:56
 */
@Entity
@Table(name = "role")
// EntityGraph(实体图)使用演示，解决查询N+1问题
@NamedEntityGraphs({
        @NamedEntityGraph(name = "role.all",
                attributeNodes = { // attributeNodes 用来定义需要懒加载的属性
                        @NamedAttributeNode(value = "users", subgraph = "articleList"), // 要懒加载users属性中的articleList元素
                },
                subgraphs = { // subgraphs 用来定义关联对象的属性,也就是对上面的 articleList 进行描述
                        @NamedSubgraph(name = "articleList", attributeNodes = @NamedAttributeNode("articleList")), // 一层延伸
                }
        ),
})
public class Role extends BaseData {

    private static final long serialVersionUID = 5012235295240129244L;

    private String roleName;//角色名

    private Integer roleType;//1： 超级管理员   2： 系统管理员 3：一般用户

    private Integer state = 1; //0禁用  1 启用

    @JsonIgnoreProperties(value = {"roles"}) //解决循环引用问题
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> users; // 与用户多对多


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
