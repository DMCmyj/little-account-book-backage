package com.catpudding.android_back_stage.system.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catpudding.android_back_stage.bill.entity.BillItem;
import com.catpudding.android_back_stage.bill.mapper.BillMapper;
import com.catpudding.android_back_stage.common.JwtUtil;
import com.catpudding.android_back_stage.common.MD5Util;
import com.catpudding.android_back_stage.common.UUIDGenerator;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.entity.SysUserRole;
import com.catpudding.android_back_stage.system.entity.SysUserRoleDom;
import com.catpudding.android_back_stage.system.mapper.UserMapper;
import com.catpudding.android_back_stage.system.mapper.UserRoleMapper;
import com.catpudding.android_back_stage.system.service.UserService;
import com.catpudding.android_back_stage.system.vo.LoginResult;
import com.catpudding.android_back_stage.system.vo.UserBillInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private BillMapper billMapper;

    @Override
    public int registeUser(SysUser sysUser) throws Exception {
        String uuid = UUIDGenerator.generate();
        sysUser.setId(uuid);
        Date date = new Date();
        sysUser.setCreateTime(date);
        sysUser.setPassword(MD5Util.MD5Encode(sysUser.getPassword(),"utf-8"));
        sysUser.setDelFlag(0);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",sysUser.getPhone());
        SysUser existUser = userMapper.selectOne(wrapper);
        if(existUser != null){
            throw new Exception("用户已存在！请登录");
        }
        int num = userMapper.insert(sysUser);
        if(num > 0){
            SysUserRoleDom sysUserRoleDom = new SysUserRoleDom();
            sysUserRoleDom.setUserId(uuid);
            sysUserRoleDom.setRoleId(2);
            userRoleMapper.insert(sysUserRoleDom);
        }
        return num;
    }

    @Override
    public LoginResult login(String phone, String password) throws Exception {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        SysUser user = userMapper.selectOne(wrapper);
        if(user == null){
            throw new Exception("用户不存在！");
        }

        String passwordMD5 = MD5Util.MD5Encode(password,"utf-8");
        LoginResult result = null;
        if(passwordMD5.equals(user.getPassword())){
            List<SysUserRole> sysUserRoles = userMapper.getUserRole(user.getId());
            System.out.println(sysUserRoles);
            result = new LoginResult();
            result.setId(user.getId());
            result.setAvatar(user.getAvatar());
            result.setEmail(user.getEmail());
            result.setPhone(user.getPhone());
            result.setRealname(user.getRealname());
            result.setUsername(user.getUsername());
            result.setSex(user.getSex());
            result.setIsAdmin(checkIsAdmin(sysUserRoles));
            result.setToken(JwtUtil.createToken(user,sysUserRoles));

            Date now = new Date();
            user.setLastLoginTime(now);
            userMapper.updateById(user);
        }else{
            throw new Exception("用户名密码错误！");
        }
        return result;
    }

    @Override
    public LoginResult changeUsername(SysUser userNew) throws Exception {
        LoginResult result = null;
        if (userMapper.updateById(userNew)>0){
            SysUser user = userMapper.selectById(userNew.getId());
            List<SysUserRole> sysUserRoles = userMapper.getUserRole(user.getId());
            result = new LoginResult();
            result.setId(user.getId());
            result.setAvatar(user.getAvatar());
            result.setEmail(user.getEmail());
            result.setPhone(user.getPhone());
            result.setRealname(user.getRealname());
            result.setUsername(user.getUsername());
            result.setSex(user.getSex());
            result.setIsAdmin(checkIsAdmin(sysUserRoles));
            result.setToken(JwtUtil.createToken(user,sysUserRoles));
        }else{
            throw new Exception("用户名修改失败！");
        }
        return result;
    }

    @Override
    public LoginResult changePassword(SysUser userToken, String oldPwd, String newPwd) throws Exception {
        SysUser user = userMapper.selectById(userToken.getId());
        String passwordMD5 = MD5Util.MD5Encode(oldPwd,"utf-8");
        LoginResult result = null;
        if(passwordMD5.equals(user.getPassword())){
            List<SysUserRole> sysUserRoles = userMapper.getUserRole(user.getId());
            String passwordNewMD5 = MD5Util.MD5Encode(newPwd,"utf-8");
            user.setPassword(passwordNewMD5);
            if(userMapper.updateById(user) > 0){
                result = new LoginResult();
                result.setId(user.getId());
                result.setAvatar(user.getAvatar());
                result.setEmail(user.getEmail());
                result.setPhone(user.getPhone());
                result.setRealname(user.getRealname());
                result.setUsername(user.getUsername());
                result.setSex(user.getSex());
                result.setIsAdmin(checkIsAdmin(sysUserRoles));
                result.setToken(JwtUtil.createToken(user,sysUserRoles));
            }else{
                throw new Exception("修改失败！");
            }
        }else{
            throw new Exception("旧密码错误！");
        }
        return result;
    }

    @Override
    public List<SysUser> getAllUser() {
        return userMapper.getAllUser();
    }

    @Override
    public Boolean delete(String id) {
        userMapper.deleteById(id);
//        if(num > 0){
//            QueryWrapper<SysUserRoleDom> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("user_id",id);
//            userRoleMapper.delete(queryWrapper);
//        }
        return true;
    }

    @Override
    public UserBillInfo getUserBillInfo(String userId) throws Exception {
        SysUser user = userMapper.selectById(userId);
        if(user != null){
            UserBillInfo userBillInfo = new UserBillInfo();
            Date now = new Date();
            //将Date类型转化为LocalDate
            ZoneId zoneId = ZoneId.systemDefault();
            Instant instant = now.toInstant();
            LocalDate localDate1 = instant.atZone(zoneId).toLocalDate();
            Instant instant2 = user.getCreateTime().toInstant();
            LocalDate localDate2 = instant2.atZone(zoneId).toLocalDate();
            Period period = Period.between(localDate2,localDate1);
            userBillInfo.setTotalDay(period.getDays() + 1);
            QueryWrapper<BillItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId",userId);
            long count = billMapper.selectCount(queryWrapper);
            userBillInfo.setTotalBill(count);
            return userBillInfo;
        }else{
            throw new Exception("未找到该用户！");
        }
    }

    private Boolean checkIsAdmin(List<SysUserRole> sysUserRoles){
        boolean flag = false;
        for(int i = 0;i<sysUserRoles.size();i++){
            if(sysUserRoles.get(i).getRoleName().equals("管理员")){
                flag = true;
                break;
            }
        }
        return flag;
    }

}
