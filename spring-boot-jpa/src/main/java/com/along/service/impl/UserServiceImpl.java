package com.along.service.impl;

import com.along.dao.RoleDao;
import com.along.dao.UserDao;
import com.along.model.dto.UserDTO;
import com.along.model.entity.Role;
import com.along.model.entity.User;
import com.along.model.vo.UserVo;
import com.along.service.UserService;
import com.along.util.MD5Util;
import com.along.util.NullUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 14:52
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private UserDao userDao;
    private RoleDao roleDao;
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, EntityManager entityManager) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.entityManager = entityManager;
    }


    @Override
    public User save(UserDTO userDTO) {

        User user = userDTO2User(userDTO);
        //执行添加
        return userDao.save(user);
    }

    @Override
    public List<User> saveAll(List<UserDTO> list) {
        List<User> userList = new ArrayList<>();
        for (UserDTO userDTO : list) {
            User user = userDTO2User(userDTO);
            userList.add(user);
        }
        //执行批量添加
        return userDao.saveAll(userList);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {

        return userDao.findAll(pageable);
    }

    /**
     * 实例查询演示
     * 优点：通过在使用springdata jpa时可以通过Example来快速的实现动态查询，同时配合Pageable可以实现快速的分页查询功能。
     * 局限：对于非字符串属性的只能精确匹配，比如想查询在某个时间段内注册的用户信息，就不能通过Example来查询
     *
     * @param name
     * @return
     */
    @Override
    public List<User> findByNameAndSex(String name, Integer sex) {
        // 创建查询条件数据对象
        User user = new User();
        user.setName(name);
        user.setSex(sex);
        // 创建匹配器，即规定如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // 改变默认字符串匹配方式：模糊查询，即%{name}%，(默认为精确查询)
                .withIgnoreCase(true) // 改变默认大小写忽略方式：忽略大小写
                // withMatcher可以改变特定字段的查询方式
                //.withMatcher("name", ExampleMatcher.GenericPropertyMatcher::startsWith) // 模糊查询匹配开头，即{name}%
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains()) // 忽略大小写，完全模糊查询，即where (lower(name) like %{name}%)
                .withIgnorePaths("status") // 忽略属性列表，忽略的属性不参与查询过滤
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE) // 当属性为null时是否参与过滤(IGNORE,INCLUDE),默认为IGNORE（忽略）
                ;

        // 创建实例
        Example<User> example = Example.of(user, matcher);
        // Example<User> example = Example.of(user);
        // 查询
        List<User> users = userDao.findAll(example);

        return users;
    }

    /**
     * 强烈推荐，这种方法简洁，可读性强，还可以返回特定的Vo，Vo是一个接口
     *
     * @param name
     * @return
     */
    @Override
    public List<UserVo> findByNameLike(String name) {
        List<UserVo> userVos = userDao.findByNameLike("%" + name + "%");

        return userVos;
    }

    /**
     * 更新
     * @param userDTO
     * @return
     */
    @Override
    public Boolean update(UserDTO userDTO) {
        Optional<User> opt = userDao.findById(userDTO.getId());
        if (opt.isPresent()) {
            User user = opt.get();
            //信息拷贝,忽略空值
            BeanUtils.copyProperties(userDTO, user, NullUtils.getNullPropertyNames(userDTO));
            //return userDao.save(user);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id) {

        userDao.deleteById(id);
    }

    /**
     * UserDto转化为User
     *
     * @param userDTO
     * @return
     */
    private User userDTO2User(UserDTO userDTO) {
        User user = new User();
        //加密密码
        if (!StringUtils.isEmpty(userDTO.getPassword())) {
            userDTO.setPassword(MD5Util.encode(userDTO.getPassword()));
        }
        //设置权限
        String roleIds = userDTO.getRoleIds();
        if (!StringUtils.isEmpty(roleIds)) {
            List<String> roleIdList = Arrays.asList(roleIds.split(","));
            //查找到所有的权限对象
            List<Role> roleList = roleDao.findAllById(roleIdList);
            Set<Role> roles = new HashSet<>(roleList);
            user.setRoles(roles);
        }
        //信息拷贝
        BeanUtils.copyProperties(userDTO, user, NullUtils.getNullPropertyNames(userDTO));

        return user;
    }
}
