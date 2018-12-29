package com.along.controller;

import com.along.entity.Person;
import com.along.service.PersonService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: controller
 * @Author along
 * @Date 2018/12/28 18:02
 */
@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("add")
    public int addPerson(@RequestBody Person person) {
        return personService.add(person);
    }

    @GetMapping("all")
    public PageInfo findAllPerson(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return personService.findAllPerson(pageNum,pageSize);

    }

    @GetMapping("get/{name}")
    public PageInfo findByName(@PathVariable String name) {
        return personService.findByName(name);
    }

    @PostMapping("insert/batch")
    public Integer insertBatch(@RequestBody List<Person> personList) {
        return personService.insertBatch(personList);
    }

    @PostMapping("update/batch")
    public Integer updateBatch(@RequestBody List<Person> personList) {
        return personService.updateBatch(personList);
    }


}
