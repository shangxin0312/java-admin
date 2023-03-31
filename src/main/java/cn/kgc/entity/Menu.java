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
@TableName("sys_menu")
public class Menu implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String title;
    private String path;
    private String perms;
    private String component;
    private Integer type;
    private String icon;
    @TableField("orderNum")
    private Integer orderNum;
    private Date created;
    private Date updated;
    private Integer statu;

    //为了便于页面显示额外增加的属性
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
}
