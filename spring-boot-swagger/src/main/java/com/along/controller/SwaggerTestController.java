package com.along.controller;

import com.along.entity.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: swagger 测试
 * @Author along
 * @Date 2020/1/11 19:54
 */
@RestController
@Api(value = "用户管理", description = "用户管理接口")
@CrossOrigin // 支持跨域
public class SwaggerTestController {


    /**
     * 增加人物
     * @return
     */
    @PostMapping(value = "employee")
    @ApiOperation(value = "新增一个用户",notes = "新增之后返回对象")
    @ApiImplicitParam(paramType = "query",name = "user",value = "用户",required = true)
    @ApiResponse(code = 400,message = "参数没有填好",response = String.class)
    public String insert(User user){
        return "新增完毕";
    }

    /**
     * 删除单个用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "employee/{id}")
    @ApiOperation(value = "删除用户",notes = "根据成员id删除单个用户")
    @ApiImplicitParam(paramType = "path",name = "id",value = "用户id",required = true,dataType = "Integer")
    @ApiResponse(code = 400,message = "参数没有填好",response = String.class)
    public String delete(@PathVariable("id")Integer id){
        return "id:" + id;
    }

    /**
     * 获取所有成员,升序排列
     * @return
     */
    @GetMapping(value = "employee/sort")
    @ApiOperation(value = "查询全部用户",notes = "默认根据升序查询全部用户信息")
    public List<User> findAll(){
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User(1L, "along" + i, i);
            list.add(user);
        }
        return list;
    }

    /**
     * 获取所有成员,升序排列
     * @return
     */
    @GetMapping(value = "employee/pageSort")
    @ApiOperation(value = "查询用户信息",notes = "查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "sort",value = "排序方式:asc|desc",dataType = "String",required = true),
            @ApiImplicitParam(paramType = "query",name = "pagenumber",value = "第几页",dataType = "Integer",required = true),
            @ApiImplicitParam(paramType = "query",name = "pageSize",value = "分页数",dataType = "Integer",required = true)
    })
    public String findAllByPage(String sort,Integer pagenumber,Integer pageSize){

        return "sort:" + sort + "   pagenumber:" + pagenumber + "   pageSize:" + pageSize;
    }
    /**
     * 根据用户名查找单个用户
     * @param username
     * @return
     */
    @GetMapping(value = "employee/find/{username}")
    @ApiOperation(value = "查询用户信息",notes = "根据用户登录名查询该用户信息")
    @ApiImplicitParam(paramType = "path",name = "username",value = "用户登录名",required = true,dataType = "String")
    public User findByUsername(@PathVariable("username") String username){
        return new User(1L, "along", 20);
    }



}
