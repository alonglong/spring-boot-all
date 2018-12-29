package com.along.service;

import com.along.entity.Person;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2018/12/28 17:42
 */
public interface PersonService {

    Integer add(Person person);

    PageInfo<Person> findAllPerson(int pageNum, int pageSize);

    PageInfo<Person> findByName(String name);

    int insertBatch(List<Person> list);

    int updateBatch(List<Person> list);


}
