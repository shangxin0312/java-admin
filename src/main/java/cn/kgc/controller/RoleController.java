package cn.kgc.controller;

import cn.kgc.entity.Role;
import cn.kgc.entity.RoleMenu;
import cn.kgc.entity.UserRole;
import cn.kgc.service.RoleMenuService;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserRoleService;
import cn.kgc.utils.PageResult;
import cn.kgc.utils.Result;
import cn.kgc.utils.StatusCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin           //允许跨域访问
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMenuService roleMenuService;

    @GetMapping("/all")
    public Result allRoleList(){
        return Result.success(roleService.queryAll());
    }

    @GetMapping("/list")
    public Result roleList(String name, @RequestParam(defaultValue = "1") Integer pageId,
                                        @RequestParam(defaultValue = "5") Integer pageSize){
        Page<Role> pageInfo = new Page<>(pageId,pageSize);
        roleService.page(pageInfo,new QueryWrapper<Role>().like(StringUtils.isNotBlank(name),"name",name)
                                                          .orderByDesc("created"));
        return Result.success(new PageResult<>(pageInfo.getTotal(),pageInfo.getRecords()));
    }

    @GetMapping("/{roleId}")
    public Result roleInfo(@PathVariable Integer roleId){
        //先查询出角色对象
        Role role = roleService.queryRole(roleId);
        //查询该角色对应的菜单
        List<RoleMenu> roleMenuList =
                roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        //提取出所有菜单id
       /*List<Integer> menuIds = new ArrayList<>();
        for (RoleMenu rm: roleMenuList) {
            menuIds.add(rm.getMenuId());
        }*/
        List<Integer> menuIds = roleMenuList.stream().map(rm -> rm.getMenuId()).collect(Collectors.toList());
        //将菜单id保存到role对象中
        role.setMenuIds(menuIds);

        return Result.success(role);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Result roleAdd(@RequestBody Role role){
        return roleService.addRole(role) ? Result.success(null) : Result.fail(StatusCode.SAVE_ROLE_FAIL,"保存菜单失败");
    }

    @PutMapping("/save")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Result roleUpdate(@RequestBody Role role){
        return roleService.modifyRole(role) ? Result.success(null) : Result.fail(StatusCode.SAVE_ROLE_FAIL,"修改角色失败");
    }


    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Result roleDelete(@PathVariable Integer roleId){
        return roleService.removeRole(roleId) ? Result.success(null) : Result.fail(StatusCode.DELETE_ROLE_FAIL,"删除角色失败");
    }

    @Transactional
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Result grantPerm(@PathVariable Integer roleId,@RequestBody Integer[] menuIds){
        //删除该角色原来所拥有的所有菜单权限
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",roleId));

        //再往关联表中插入最新的菜单信息
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(null,roleId,menuId);
            roleMenuService.save(roleMenu);
        }

        return Result.success(null);
    }

}
