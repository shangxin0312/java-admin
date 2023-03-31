package cn.kgc.service.impl;

import cn.kgc.entity.AccountUser;
import cn.kgc.entity.Role;
import cn.kgc.entity.User;
import cn.kgc.mapper.UserMapper;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //根据用户id查询该用户所拥有的角色
       String authorityInfo = userService.getUserAuthorityInfo(user.getId());

        return new AccountUser(user.getId(),
                               user.getUsername(),
                               user.getRealName(),
                               user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorityInfo));
    }
}
