package com.along.service;

import com.along.model.dto.RoleDTO;
import com.along.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 14:56
 */
public interface RoleService {

    Role save(RoleDTO roleDTO);

    List<Role> saveAll(List<RoleDTO> list);

    Page<Role> findAll(Pageable pageable);

    Role findByName(String name);

    Role findByRoleType(String name);

    Role update(Role role);
}
