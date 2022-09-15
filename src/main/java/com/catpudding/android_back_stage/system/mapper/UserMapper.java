package com.catpudding.android_back_stage.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<SysUser> {
    List<SysUserRole> getUserRole(@Param("userId") String userId);
    List<SysUser> getAllUser();
}
