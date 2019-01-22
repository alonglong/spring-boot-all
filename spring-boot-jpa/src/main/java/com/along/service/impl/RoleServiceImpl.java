package com.along.service.impl;

import com.along.dao.RoleDao;
import com.along.dao.UserDao;
import com.along.model.dto.RoleDTO;
import com.along.model.entity.Role;
import com.along.model.entity.User;
import com.along.service.RoleService;
import com.along.util.NullUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 15:01
 */
@Service(value = "roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    private UserDao userDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }

    @Override
    public Role save(RoleDTO roleDTO) {
        Role role = new Role();
        //设置用户
        String userIds = roleDTO.getUserIds();
        if (!StringUtils.isEmpty(userIds)) {
            List<String> userIdList = Arrays.asList(userIds.split(","));
            List<User> userList = userDao.findAllById(userIdList);
            Set<User> users = new HashSet<>(userList);
            role.setUsers(users);
        }
        //信息拷贝
        BeanUtils.copyProperties(roleDTO, role, NullUtils.getNullPropertyNames(roleDTO));
        //执行添加
        return roleDao.save(role);
    }

    @Override
    public List<Role> saveAll(List<RoleDTO> list) {
        return null;
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleDao.findAll(pageable);
    }

    @Override
    public Role findByName(String name) {
        return null;
    }

    @Override
    public Role findByRoleType(String name) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }
}
