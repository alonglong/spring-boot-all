package com.along.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: User参数接收类
 * @Author along
 * @Date 2019/1/9 15:29
 */
public class UserDTO {

    @NotNull(message = "id不能为空", groups = {ParameterGroup2.class})
    private String id;

    @NotEmpty(message = "用户名不能为空", groups = {ParameterGroup1.class, ParameterGroup2.class})
    private String name;

    @NotEmpty(message = "用户密码不能为空", groups = {ParameterGroup1.class, ParameterGroup2.class})
    private String password;

    @NotNull(message = "性别不能为空", groups = {ParameterGroup1.class, ParameterGroup2.class})
    private Integer sex;

    private Integer status = 1; //-1：删除 0: 禁用 1：启用

    @Email(message = "邮箱格式错误", groups = {ParameterGroup1.class, ParameterGroup2.class})
    private String email;

    @NotNull(message = "生日不能为空", groups = {ParameterGroup1.class, ParameterGroup2.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String roleIds; // 角色id，多个id用“,”分隔



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }


    /**
     * 验证组1,添加
     */
    public interface ParameterGroup1 {
    }

    /**
     * 验证组2,修改
     */
    public interface ParameterGroup2 {
    }
}
