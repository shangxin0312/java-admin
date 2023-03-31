package cn.kgc.service.impl;

import cn.kgc.entity.Role;
import cn.kgc.entity.UserRole;
import cn.kgc.mapper.RoleMapper;
import cn.kgc.mapper.UserRoleMapper;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
