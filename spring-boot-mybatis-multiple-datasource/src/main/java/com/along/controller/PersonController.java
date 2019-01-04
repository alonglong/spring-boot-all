package com.along.controller;

import com.along.entity.Person;
import com.along.service.PersonService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: controller
 * @Author along
 * @Date 2018/12/28 18:02
 */
@RestController
@RequestMapping("person")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(@Qualifier("personService") PersonService personService) {
        Assert.notNull(personService, "personService must not be null");
        this.personService = personService;
    }

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

    @PostMapping("insert")
    public Integer insert(@RequestBody Person person) {
        return personService.insert(person);
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
