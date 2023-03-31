package cn.kgc.service;

import cn.kgc.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {
    //获取所有菜单列表
    public List<Menu> getAllMenus();

    //删除菜单
    public boolean removeMenu(Integer menuId);

    //添加菜单
    public boolean saveMenu(Menu menu);

    //修改菜单
    public boolean modifyMenu(Menu menu);
}
