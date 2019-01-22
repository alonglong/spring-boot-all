package com.along.model.dto;

/**
 * @Description: Role参数接收类
 * @Author along
 * @Date 2019/1/9 15:34
 */
public class RoleDTO {

    private String id;

    private String roleName;//角色名

    private Integer roleType;//1： 超级管理员   2： 系统管理员 3：一般用户

    private Integer state = 1; //0禁用  1 启用

    private String userIds; // 用户id,用“,”分隔




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}
