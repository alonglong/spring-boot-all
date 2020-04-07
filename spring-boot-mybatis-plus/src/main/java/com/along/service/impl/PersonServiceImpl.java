package com.along.service.impl;

import com.along.entity.Person;
import com.along.mapper.PersonMapper;
import com.along.service.PersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Along
 * @since 2020-04-06
 */
@Service(value = "personService")
@Transactional
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

    @Override
    public void findPerson(Page<Person> page, Person person) {

        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");

        Long id = person.getId();
        String name = person.getName();
        Integer age = person.getAge();
        String address = person.getAddress();

        if (!StringUtils.isEmpty(id)) {
            queryWrapper.eq("id", id);
        } else {
            if (!StringUtils.isEmpty(name)) {
                queryWrapper.like("name", name);
            }
            if (!StringUtils.isEmpty(age)) {
                queryWrapper.like("age", age);
            }
            if (!StringUtils.isEmpty(address)) {
                queryWrapper.like("address", address);
            }
        }

        baseMapper.selectPage(page, queryWrapper);

    }

}
