package com.along.service;

import com.along.entity.Person;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Along
 * @since 2020-04-06
 */
public interface PersonService extends IService<Person> {

    /**
     * 查询全部
     * @param page
     * @return
     */
    void findPerson(Page<Person> page, Person person);


}
