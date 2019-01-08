package com.along.dao;

import com.along.entity.Person;
import com.along.entity.PersonExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "personMapper")
public interface PersonMapper {
    long countByExample(PersonExample example);

    int deleteByExample(PersonExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Person record);

    int insertSelective(Person record);

    List<Person> selectByExample(PersonExample example);

    Person selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Person record, @Param("example") PersonExample example);

    int updateByExample(@Param("record") Person record, @Param("example") PersonExample example);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatchSelective(List list);

    /**
     * 批量更新
     * @param list
     * @return
     */
    int updateBatchByPrimaryKeySelective(List list);
}