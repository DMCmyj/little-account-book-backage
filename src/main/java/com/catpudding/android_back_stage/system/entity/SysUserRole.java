package com.catpudding.android_back_stage.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class SysUserRole {

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "role_name")
    private String roleName;

}
