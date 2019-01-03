package com.along.service;

import com.along.entity.Person;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Description: service接口
 * @Author along
 * @Date 2018/12/28 17:42
 */
public interface PersonService {

    /**
     * 添加
     * @param person
     * @return
     */
    Integer add(Person person);

    /**
     * 查询全部
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Person> findAllPerson(int pageNum, int pageSize);

    /**
     * 根据名字查询
     * @param name
     * @return
     */
    PageInfo<Person> findByName(String name);

    /**
     * 单条插入
     * @param person
     * @return
     */
    int insert(Person person);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatch(List<Person> list);

    /**
     * 批量更新
     * @param list
     * @return
     */
    int updateBatch(List<Person> list);


}
