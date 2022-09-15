package com.catpudding.android_back_stage.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_user_role")
public class SysUserRoleDom {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "role_id")
    private int roleId;
}
