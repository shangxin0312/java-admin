package cn.kgc.service.impl;

import cn.kgc.entity.Menu;
import cn.kgc.entity.RoleMenu;
import cn.kgc.mapper.MenuMapper;
import cn.kgc.mapper.RoleMenuMapper;
import cn.kgc.service.MenuService;
import cn.kgc.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> getAllMenus() {
        //先从Redis在加载所有菜单
        List menus = redisUtil.lGet("menus",0,-1);

        if(menus == null || menus.size() == 0){
            menus = menuMapper.selectList(new QueryWrapper<Menu>().orderByAsc("parent_id","orderNum"));
            //将查到的数据保存到redis中
            redisUtil.lSet("menus",menus);
        }
         //填充子菜单属性
        return setChildren(menus);
    }
    //给每个菜单填充该菜单的children属性值
    private List<Menu> setChildren(List<Menu> menuList){
        List<Menu> result = new ArrayList<>();
        for(Menu menu : menuList){
            for(Menu child : menuList){
                if(child.getParentId() == menu.getId()){
                    menu.getChildren().add(child);
                }
            }

            //只需要返回第一级菜单
            if(menu.getParentId() == 0){
                result.add(menu);
            }
        }
        return result;
    }

    @Transactional
    public boolean removeMenu(Integer menuId) {
        //再删除角色-菜单关联表中的数据
        roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("menu_id",menuId));

        //然后删除菜单表记录
        menuMapper.deleteById(menuId);

        //最后删除缓存中的数据
        List menus = redisUtil.lGet("menus", 0, -1);
        menus.stream().forEach(menu -> {
            if (((Menu)menu).getId().equals(menuId)){
                redisUtil.lRemove("menus",1,menu);
            }
        });

        return true;
    }

    @Transactional
    public boolean saveMenu(Menu menu) {
        //完善数据
        menu.setCreated(new Date());
        //往数据库中插入记录
        menuMapper.insert(menu);

        //往缓存中插入数据
        return redisUtil.lSet("menus",menu);
    }

    @Transactional
    public boolean modifyMenu(Menu menu) {
        //完善数据
        menu.setUpdated(new Date());
        //修改数据库中的记录
        menuMapper.updateById(menu);

        //更新缓存中的数据
        List menus = redisUtil.lGet("menus", 0, -1);
        for(int i=0; i<menus.size(); i++){
            if(((Menu)menus.get(i)).getId().equals(menu.getId())){
                return redisUtil.lUpdateIndex("menus",i,menu);
            }
        }
        return true;
    }
}
