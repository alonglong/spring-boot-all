package com.along.dao;

import com.along.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 14:10
 */
public interface RoleDao extends JpaRepository<Role, String> {

    //重写findAll(Pageable pageable)方法,用实体图查询
    @EntityGraph(value = "role.all", type = EntityGraph.EntityGraphType.FETCH)
    Page<Role> findAll(Pageable pageable);

}
