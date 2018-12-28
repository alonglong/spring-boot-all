package com.along.controller;

import com.along.entity.Person;
import com.along.mapper.PersonMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2018/12/27 14:51
 */
@RestController
@RequestMapping("person")
public class PersonController {

    @SuppressWarnings("all")
    @Autowired
    private PersonMapper personMapper;

    @GetMapping("list/{pageNum}/{pageSize}")
    public PageInfo list(@PathVariable int pageNum, @PathVariable int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<Person> list = personMapper.list();
        return new PageInfo<>(list);
    }

    @GetMapping("list/{name}")
    public List<Person> listByName(@PathVariable("name") String name) {
        return personMapper.listByName(name);
    }

    @GetMapping("listSample")
    public List<Person> listSample() {
        return personMapper.listSample();
    }

    @GetMapping("get/{name}/{address}/{pageNum}/{pageSize}")
    public PageInfo get(@PathVariable String name, @PathVariable String address,
                        @PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Person> personList = personMapper.get(name, address);
        return new PageInfo<>(personList);
    }

    @GetMapping("get/{address}/{pageNum}/{pageSize}")
    public PageInfo getPersonByAddress(@PathVariable String address,
                                           @PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Person> personByAddress = personMapper.getByAddress(address);
        return new PageInfo<>(personByAddress);
    }

    /**
     * 单个添加
     * @param person
     * @return
     */
    @PostMapping("save")
    public Integer save(@RequestBody Person person) {
        return personMapper.save(person);
    }

    /**
     * 批量添加
     * @param personList
     * @return
     */
    @PostMapping("saveBatch")
    public Integer save(@RequestBody List<Person> personList) {
        return personMapper.saveBatch(personList);
    }






}
