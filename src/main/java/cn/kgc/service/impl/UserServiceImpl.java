package cn.kgc.service.impl;

import cn.kgc.entity.Menu;
import cn.kgc.entity.Role;
import cn.kgc.entity.User;
import cn.kgc.mapper.MenuMapper;
import cn.kgc.mapper.UserMapper;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RoleService roleService;
    @Resource
    private MenuMapper menuMapper;
    @Override
    public String getUserAuthorityInfo(Integer userId) {
        StringBuffer authorities = new StringBuffer();
        //查询角色
        List<Role> roles = roleService.queryRoleList(userId);
        if (roles != null && roles.size() > 0){
            String roleAuthority = roles.stream().map(role -> "ROLE_"+role.getCode()).collect(Collectors.joining(","));
            authorities.append(roleAuthority);
        }

        //查询菜单权限
        List<Integer> menuIdList = menuMapper.getMenuIdList(userId);
        if (menuIdList != null && menuIdList.size() > 0){
            List<Menu> menuList = menuMapper.selectBatchIds(menuIdList);
            String menuAuthority = menuList.stream().map(menu -> menu.getPerms()).collect(Collectors.joining(","));
            authorities.append(",").append(menuAuthority);

        }
        return authorities.toString();
    }
}
