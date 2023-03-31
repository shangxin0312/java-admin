package cn.kgc.service.impl;

import cn.kgc.entity.Menu;
import cn.kgc.entity.RoleMenu;
import cn.kgc.mapper.MenuMapper;
import cn.kgc.mapper.RoleMenuMapper;
import cn.kgc.service.MenuService;
import cn.kgc.service.RoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
