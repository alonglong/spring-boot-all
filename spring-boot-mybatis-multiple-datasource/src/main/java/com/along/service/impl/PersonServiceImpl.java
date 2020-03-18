package com.along.service.impl;

import com.along.annotation.DataSource;
import com.along.common.ContextConst;
import com.along.dao.PersonMapper;
import com.along.entity.Person;
import com.along.entity.PersonExample;
import com.along.service.PersonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: service实现
 * @Author along
 * @Date 2018/12/28 17:44
 */
@Service(value = "personService")
//尝试解决mysql数据源切换到oracle数据源出错的问题，在类上加事务，需要时可尝试在方法上加事务
//@Transactional
public class PersonServiceImpl implements PersonService {

    private PersonMapper personMapper;

    @Autowired
    public PersonServiceImpl(@Qualifier("personMapper") PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Override
    public Integer add(Person person) {
        return personMapper.insert(person);
    }

    @Override
    public PageInfo<Person> findAllPerson(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PersonExample example = new PersonExample();
        List<Person> personList = personMapper.selectByExample(example);
        return new PageInfo<>(personList);
    }

    @DataSource(ContextConst.DataSourceType.PROD) // 指定该方法使用prod数据源
    @Override
    public PageInfo<Person> findByName(String name) {
        PersonExample example = new PersonExample();
        PersonExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Person> personList = personMapper.selectByExample(example);
        return new PageInfo<>(personList);
    }

    @DataSource(ContextConst.DataSourceType.LOCAL) // 指定该方法使用local数据源
    @Override
    public int insert(Person person) {

        return personMapper.insert(person);
    }


    @Override
    public int insertBatch(List<Person> list) {
        return personMapper.insertBatchSelective(list);
    }

    @Override
    public int updateBatch(List<Person> list) {
        return personMapper.updateBatchByPrimaryKeySelective(list);

    }


}
