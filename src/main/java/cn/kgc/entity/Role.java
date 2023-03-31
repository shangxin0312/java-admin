package cn.kgc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
public class Role implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String code;
    private String remark;
    private Date created;
    private Date updated;
    private Integer statu;

    //增加属性,用来保存角色所对应的菜单id
    @TableField(exist = false)
    private List<Integer> menuIds = new ArrayList<>();
}
