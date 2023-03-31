package cn.kgc.service;

import cn.kgc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    /**
     * 根据id获取用户的权限编码(包含角色编码及菜单权限)
     * @param userId 用户id
     * @return 权限编码
     */
    String getUserAuthorityInfo(Integer userId);
}
