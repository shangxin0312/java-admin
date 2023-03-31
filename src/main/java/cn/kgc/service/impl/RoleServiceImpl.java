package cn.kgc.service.impl;

import cn.kgc.entity.Role;
import cn.kgc.entity.RoleMenu;
import cn.kgc.entity.User;
import cn.kgc.entity.UserRole;
import cn.kgc.mapper.RoleMapper;
import cn.kgc.mapper.RoleMenuMapper;
import cn.kgc.mapper.UserMapper;
import cn.kgc.mapper.UserRoleMapper;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserService;
import cn.kgc.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> queryRoleList(Integer userId) {
        return roleMapper.selectList(new QueryWrapper<Role>().inSql("id","select role_id from sys_user_role where user_id = "+userId));
    }

    //先查询缓存，如果缓存中有则直接重用缓存数据;如果缓存没有，再执行方法体并将方法的返回值保存到缓存中
    @Cacheable(cacheNames = {"roleCache"},key = "'roles'")
    public List<Role> queryAll() {
        return roleMapper.selectList(null);
    }

    @Cacheable(cacheNames = "roleCache",key = "#roleId")
    public Role queryRole(Integer roleId) {
        return roleMapper.selectById(roleId);
    }

    //@CachePut(cacheNames = "roleCache",key="#role.id")
    //@CacheEvict(cacheNames = "roleCache",key = "#role.id")
    @Caching(evict = {@CacheEvict(value = "roleCache",key = "'roles'"),
                      @CacheEvict(value = "roleCache",key = "#role.id")
    })
    public boolean modifyRole(Role role) {
        //完善数据
        role.setUpdated(new Date());
        return roleMapper.updateById(role) > 0;
    }

    //@CacheEvict(cacheNames = "roleCache",key = "#roleId")
    @Transactional
    @Caching(evict = {@CacheEvict(value = "roleCache",key = "'roles'"),
                      @CacheEvict(value = "roleCache",key = "#roleId")
    })
    public boolean removeRole(Integer roleId) {
        //先删除角色-菜单关联表中的数据
        roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        //再删除该用户-角色关联表中的数据
        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("role_id",roleId));

        //最后再删除角色
        return roleMapper.deleteById(roleId) > 0;
    }

    @CacheEvict(cacheNames = "roleCache",key = "'roles'")
    public boolean addRole(Role role) {
        role.setCreated(new Date());   //完善数据
        return roleMapper.insert(role) > 0;
    }
}
