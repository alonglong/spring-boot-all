package com.along.controller;

import com.along.model.dto.RoleDTO;
import com.along.model.entity.Role;
import com.along.model.entity.User;
import com.along.model.vo.ResultMapper;
import com.along.service.RoleService;
import com.along.util.ResultMapperUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 18:05
 */
@RestController
@RequestMapping("role")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/save")
    public ResultMapper<Role> save(@RequestBody RoleDTO roleDTO) {
        Role save = roleService.save(roleDTO);
        if (null == save) {
            return ResultMapperUtil.error(406,"添加失败");
        }
        return ResultMapperUtil.success(save);
    }

    @GetMapping("/all")
    public ResultMapper list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "20") Integer size) {
        //分页+排序查询演示：
        //Pageable pageable = new PageRequest(page, size);//2.0版本后,该方法以过时
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime","createTime");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Role> roles = roleService.findAll(pageable);
        return ResultMapperUtil.success(roles);
    }
}
