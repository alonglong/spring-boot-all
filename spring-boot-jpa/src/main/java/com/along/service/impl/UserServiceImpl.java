package com.along.service.impl;

import com.along.dao.RoleDao;
import com.along.dao.UserDao;
import com.along.model.dto.UserDTO;
import com.along.model.entity.Article;
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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    @Override
    public List<User> findByName(String name) {
        List<User> users = userDao.findByName(name);
        return users;
    }

    /**
     * 更新
     *
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
     *
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

    // ---------------------使用Specifications动态构建查询：
    @Override
    public List<User> findUserByNameAndSex0(String name, Integer sex) {
        // 1. 构建出CriteriaQuery类型的参数
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        // 2. 获取User的Root，也就是包装对象
        Root<User> root = query.from(User.class);
        // 3. 构建查询条件，这里相当于where user.id = id;
        Predicate predicate = builder.and(
                builder.like(root.get("name").as(String.class), "%" + name + "%"),
                builder.equal(root.get("sex").as(Integer.class), sex)
        );
        query.where(predicate); // 到这里一个完整的动态查询就构建完成了
        // 指定查询结果集，相当于“select id，name...”，如果不设置，默认查询root中所有字段
        query.select(root);
        // 5. 执行查询,获取查询结果
        TypedQuery<User> typeQuery = entityManager.createQuery(query);
        List<User> resultList = typeQuery.getResultList();

        return resultList;
    }

    /**
     * 根据name查询
     *
     * @param name
     * @return
     */
    @Override
    public List<User> findUserByName(String name) {
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("name"), name);
                return predicate;
            }
        });
    }

    /**
     * 根据name和sex查询,方式一
     *
     * @param name
     * @param sex
     * @return
     */
    @Override
    public List<User> findUserByNameAndSex1(String name, Integer sex) {
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("name"), name));
                list.add(criteriaBuilder.equal(root.get("sex"), sex));

                Predicate[] predicates = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(predicates));
            }
        });
    }

    /**
     * 根据name和sex查询,方式二（个人推荐）
     *
     * @param name
     * @param sex
     * @return
     */
    @Override
    public List<User> findUserByNameAndSex2(String name, Integer sex) {
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("name"), name),
                        criteriaBuilder.equal(root.get("sex"), sex)
                );
            }
        });
    }

    /**
     * in 查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<User> findUserByIds(List<String> ids) {
        return userDao.findAll((Specification<User>) (root, query, criteriaBuilder) -> root.in(ids));
    }

    /**
     * 动态语句查询
     *
     * @param user
     * @return
     */
    @Override
    public Page<User> findUser(User user, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime", "createTime");
        Pageable pageable = PageRequest.of(page, size, sort);
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (!StringUtils.isEmpty(user.getId())) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), user.getId()));
                }
                if (!StringUtils.isEmpty(user.getName())) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), user.getName()));
                }
                if (null != user.getCreateTime()) {
                    predicateList.add(criteriaBuilder.greaterThan(root.get("createTime"), user.getCreateTime()));
                }
                if (null != user.getUpdateTime()) {
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("updateTime"), user.getUpdateTime()));
                }

                Predicate[] predicateArr = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicateArr));
            }
        }, pageable);
    }


    /**
     * 表关联查询
     * @param articleId
     * @param roleId
     * @return
     */
    public List<User> findUserByArticleAndRole(String articleId, String roleId) {
        return userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 方式1
                ListJoin<User, Article> articleJoin = root.join(root.getModel().getList("articleList", Article.class), JoinType.LEFT);
                SetJoin<User, Role> roleJoin = root.join(root.getModel().getSet("roles", Role.class), JoinType.LEFT);
                // 方式2
                //Join<User, Article> articleJoin = root.join("articleList", JoinType.LEFT);
                //Join<User, Role> roleJoin = root.join("roles", JoinType.LEFT);

                Predicate predicate = criteriaBuilder.and(
                        criteriaBuilder.equal(articleJoin.get("id"), articleId),
                        criteriaBuilder.equal(roleJoin.get("id"), roleId)
                );
                return predicate;
            }
        });

    }


}
