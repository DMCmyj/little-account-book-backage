package com.catpudding.android_back_stage.system.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catpudding.android_back_stage.system.entity.SysUserRole;
import com.catpudding.android_back_stage.system.entity.SysUserRoleDom;
import com.catpudding.android_back_stage.system.mapper.UserRoleMapper;
import com.catpudding.android_back_stage.system.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, SysUserRoleDom> implements UserRoleService {

}
