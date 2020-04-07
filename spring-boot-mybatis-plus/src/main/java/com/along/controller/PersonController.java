package com.along.controller;


import com.along.entity.Person;
import com.along.service.PersonService;
import com.along.vo.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Along
 * @since 2020-04-06
 */
@Api(value = "用户管理接口", description = "用户管理")
@RestController
@RequestMapping("/person")
@CrossOrigin
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("save")
    @ApiOperation(value = "单个用户添加")
    public R addPerson(@ApiParam(name = "用户对象", value = "传入json格式", required = true) @RequestBody Person person) {
        boolean save = personService.save(person);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("/list/{page}/{limit}")
    @ApiOperation(value = "查询全部用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "page", value = "第几页", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = "path", name = "limit", value = "分页数", dataType = "Long", required = true)
    })
    public R findPerson(@PathVariable Long page, @PathVariable Long limit,
                        @ApiParam(name = "用户对象", value = "传入json格式") Person person) {

        Page<Person> pageParam = new Page<>(page, limit);
        personService.findPerson(pageParam, person);

        List<Person> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);

    }


    @ApiOperation(value = "批量添加")
    @PostMapping("save/batch")
    public R insertBatch(
            @ApiParam(name = "用户对象数组", value = "json", required = true)
            @RequestBody List<Person> personList) {
        boolean b = personService.saveBatch(personList);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "批量更新")
    @PostMapping("update/batch")
    public R updateBatch(
            @ApiParam(name = "用户对象数组", value = "json", required = true)
            @RequestBody List<Person> personList) {
        boolean b = personService.updateBatchById(personList);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }

    }

}

