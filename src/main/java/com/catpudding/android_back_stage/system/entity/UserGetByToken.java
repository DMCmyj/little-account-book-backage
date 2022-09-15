package com.catpudding.android_back_stage.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserGetByToken {
    private SysUser sysUser;
    private List<SysUserRole> roles;
}
