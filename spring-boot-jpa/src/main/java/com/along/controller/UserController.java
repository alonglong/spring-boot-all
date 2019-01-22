package com.along.controller;

import com.along.model.dto.UserDTO;
import com.along.model.entity.User;
import com.along.model.vo.ResultMapper;
import com.along.model.vo.UserVo;
import com.along.service.UserService;
import com.along.util.ResultMapperUtil;
import net.bytebuddy.description.field.FieldDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 15:20
 */
@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResultMapper save(@RequestBody @Validated(UserDTO.ParameterGroup1.class) UserDTO userDTO) {
        User save = userService.save(userDTO);
        if (null == save) {
            return ResultMapperUtil.error(406, "添加失败");
        }
        return ResultMapperUtil.success(save);
    }

    @PostMapping("/save/batch")
    public ResultMapper saveBatch(@RequestBody List<UserDTO> list) {
        List<User> save = userService.saveAll(list);
        if (StringUtils.isEmpty(save.size())) {
            return ResultMapperUtil.error(406, "添加失败");
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
        Page<User> users = userService.findAll(pageable);
        return ResultMapperUtil.success(users);
    }

    @GetMapping("/findByNameAndSex/{name}/{sex}")
    public ResultMapper findByNameAndSex(@PathVariable String name, @PathVariable Integer sex) {
        List<User> users = userService.findByNameAndSex(name, sex);
        return ResultMapperUtil.success(users);
    }

    @GetMapping("/findByName/{name}")
    public ResultMapper findByNameLike(@PathVariable String name) {
        List<UserVo> userVos = userService.findByNameLike(name);
        return ResultMapperUtil.success(userVos);
    }

}
