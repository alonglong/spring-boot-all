package com.along.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Description: 文章实体类
 * @Author along
 * @Date 2019/1/8 17:38
 */
@Entity
@Table(name = "article")
public class Article extends BaseData{

    private static final long serialVersionUID = -4817984675096952797L;

    @NotEmpty(message = "标题不能为空")
    @Column(nullable = false, length = 50)
    private String title;

    @Lob // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch = FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content; // 文章全文内容

    /**
     * 多对一配置演示：
     * 可选属性optional=false,表示sysUser不能为空
     * 配置了级联更新（合并）和刷新，删除文章，不影响用户
     */
    @JsonIgnoreProperties(value = {"articleList","roles"}) //解决循环引用问题
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "user_id") // 设置在article表中的关联字段(外键)名
    private User user; // 所属用户


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
