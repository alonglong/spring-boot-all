package com.along.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 实体类基类
 * @Author along
 * @Date 2019/1/8 16:22
 */
@MappedSuperclass
public abstract class BaseData implements Serializable {

    private static final long serialVersionUID = -3013776712039356819L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    void createdAt() {
        this.createTime = this.updateTime = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updateTime = new Date();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
