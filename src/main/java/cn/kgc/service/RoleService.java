package cn.kgc.service;

import cn.kgc.entity.Role;
import cn.kgc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    /**
     * 根据用户id查询该用户所拥有的角色
     * @param userId 用户id
     * @return 角色列表
     */
    public List<Role> queryRoleList(Integer userId);

    /**
     * 查询所有角色
     * @return 角色列表集合
     */
    public List<Role> queryAll();

    /**
     * 根据角色id查询角色对象
     * @param roleId
     * @return
     */
    public Role queryRole(Integer roleId);

    /**
     * 添加角色
     * @param role
     * @return
     */
    public boolean addRole(Role role);

    /**
     * 修改角色
     * @param role
     * @return
     */
    public boolean modifyRole(Role role);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    public boolean removeRole(Integer roleId);


}
