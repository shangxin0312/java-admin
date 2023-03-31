package cn.kgc.controller;

import cn.kgc.dto.UserInfoDto;
import cn.kgc.entity.Role;

import cn.kgc.entity.User;
import cn.kgc.entity.UserRole;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserRoleService;
import cn.kgc.service.UserService;
import cn.kgc.utils.PageResult;
import cn.kgc.utils.Result;
import cn.kgc.utils.StatusCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

//@CrossOrigin    //允许跨域访问
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public Result userList(String realName,String mobile,
                           @RequestParam(defaultValue = "1") Integer pageId,
                           @RequestParam(defaultValue = "5")Integer pageSize){
        Page<User> pageInfo = new Page<>(pageId,pageSize);
        userService.page(pageInfo,new QueryWrapper<User>().like(StringUtils.isNotBlank(realName),"real_name",realName)
                .likeRight(StringUtils.isNotBlank(mobile),"mobile",mobile)
                .orderByDesc("created")
        ) ;
        //填充每个用户角色所拥有的角色
        for (User user:pageInfo.getRecords()){
            user.setRoles(roleService.queryRoleList(user.getId()));
        }

        return Result.success(new PageResult<>(pageInfo.getTotal(),pageInfo.getRecords()));
    }

    @GetMapping("/{userId}")
    public Result userInfo(@PathVariable Integer userId){
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));
        //填充该用户所拥有的角色
        List<Role> userRoles = roleService.queryRoleList(userId);
        user.setRoles(userRoles);

        return Result.success(user);
    }


    @GetMapping("/exists")
    public Result userExists(String username,Integer userId){
        int rows = userService.count(new QueryWrapper<User>().eq("username", username)
                .ne(userId != null ,"id",userId)
        );
        Map<String,Boolean> map = new HashMap<>();
        if (rows > 0){
            map.put("exists",true);
        }else{
            map.put("exists",false);
        }

        return Result.success(map);
    }

    @PostMapping("/save")
    //@PreAuthorize("hasRole('ROLE_admin')")
    @PreAuthorize("hasAnyAuthority('sys:user:insert')")
    public Result userAdd(@RequestBody User user){
        //完善数据
        user.setPassword(passwordEncoder.encode("123456"));
        user.setCreated(new Date());
        user.setAvatar("group1/M00/00/00/wKgHbmPvLemADc37AAJCuweOqjs05_240x180.jpeg");
        user.setStatu(1);
        //保存数据
        boolean ok = userService.save(user);
        if (ok){
            return Result.success(null);
        }else{
            return Result.fail(StatusCode.SAVE_USER_FAIL,"保存用户失败");
        }
    }

    @PutMapping("/save")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Result userUpdate(@RequestBody User user){
        //完善数据
        user.setUpdated(new Date());
        //修改记录
        boolean ok = userService.updateById(user);
        if (ok){
            return Result.success(null);
        }else{
            return Result.fail(StatusCode.UPDATE_USER_FAIL,"修改用户失败");
        }
    }

    @Transactional
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
    public Result userDelete(@PathVariable Integer userId){
        //先删除该用户所拥有的角色
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",userId));
        return userService.removeById(userId) ? Result.success(null) : Result.fail(StatusCode.DELETE_USER_FAIL,"删除用户失败");
    }

    @Transactional
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasRole('admin')")
    public Result grantRole(@PathVariable Integer userId,@RequestBody Integer[] roleIds){
        //删除该用户原来所拥有的所有角色
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",userId));

        //再往关联表中插入最新的角色信息
        for (Integer roleId : roleIds) {
            UserRole userRole = new UserRole(null,userId,roleId);
            userRoleService.save(userRole);
        }

        return Result.success(null);
    }

    //个人中心查询用户信息
    @GetMapping("/info/{username}")
    public Result showUserInfo(@PathVariable String username){
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        return Result.success(user);
    }

    //个人中心修改用户信息
    @PutMapping("/edit/{username}")
    public Result modifyUserInfo(@PathVariable String username, @RequestBody UserInfoDto userDto){
        //校验旧密码是否正确
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));

        if (!passwordEncoder.matches(userDto.getOldPassword(),user.getPassword())){
            return Result.fail(StatusCode.UPDATE_PASSWORD_FAIL,"旧密码错误");
        }

        //准备更新数据
        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
        user.setAvatar(userDto.getAvatar());
        //添加修改时间
        user.setUpdated(new Date());

        //将数据保存到数据库中
        userService.updateById(user);
        return Result.success(user);
    }
}
