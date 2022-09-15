package com.catpudding.android_back_stage.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.vo.LoginResult;
import com.catpudding.android_back_stage.system.vo.UserBillInfo;

import java.util.List;

/**
 * <p>
 *     用户表
 * </p>
 *
 * @Author 马永杰
 */
public interface UserService extends IService<SysUser> {
    public int registeUser(SysUser sysUser) throws Exception;
    public LoginResult login(String phone,String password) throws Exception;
    public LoginResult changeUsername(SysUser user) throws Exception;
    public LoginResult changePassword(SysUser user,String oldPwd,String newPwd) throws Exception;

    public List<SysUser> getAllUser();
    public Boolean delete(String id);
    public UserBillInfo getUserBillInfo(String userId) throws Exception;
}
