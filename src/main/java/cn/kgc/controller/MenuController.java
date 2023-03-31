package cn.kgc.controller;

import cn.kgc.entity.Menu;
import cn.kgc.entity.RoleMenu;
import cn.kgc.service.MenuService;
import cn.kgc.service.RoleMenuService;
import cn.kgc.utils.Result;
import cn.kgc.utils.StatusCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//@CrossOrigin           //允许跨域访问
@RestController
@RequestMapping("/sys/menu")
public class MenuController {
    @Resource
    private MenuService menuService;


    @GetMapping("/list")
    public Result menuList(){
        List<Menu> menuList = menuService.getAllMenus();
        return Result.success(menuList);
    }

    @PostMapping("/save")
    public Result menuAdd(@RequestBody Menu menu){
        //保存数据
        return menuService.saveMenu(menu) ? Result.success(null) : Result.fail(StatusCode.SAVE_MENU_FAIL,"保存菜单失败");
    }

    @GetMapping("/{menuId}")
    public Result userInfo(@PathVariable Integer menuId){
        Menu menu = menuService.getOne(new QueryWrapper<Menu>().eq("id",menuId));
        return Result.success(menu);
    }

    @PutMapping("/save")
    public Result menuUpdate(@RequestBody Menu menu){
        //修改记录
        return menuService.modifyMenu(menu) ? Result.success(null) : Result.fail(StatusCode.SAVE_MENU_FAIL,"修改菜单失败");
    }

    @Transactional
    @DeleteMapping("/{menuId}")
    public Result menuDelete(@PathVariable Integer menuId){
        //如果该菜单存在下级菜单则删除失败
        int rows = menuService.count(new QueryWrapper<Menu>().eq("parent_id", menuId));
        if(rows > 0){
            return  Result.fail(StatusCode.DELETE_MENU_FAIL,"请先删除下级菜单");
        }
        return menuService.removeMenu(menuId) ? Result.success("删除成功！") : Result.fail(StatusCode.DELETE_MENU_FAIL,"删除菜单失败");
    }

}
