package cn.kgc.mapper;

import cn.kgc.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    @Select("select b.menu_id from  sys_user_role a,sys_role_menu b where a.role_id = b.role_id and a.user_id = #{userId}")
    List<Integer> getMenuIdList(Integer userId);
}
