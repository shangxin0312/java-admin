package cn.kgc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roleId;
    private Integer menuId;
}
