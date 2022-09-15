package com.catpudding.android_back_stage.system.controller;


import com.catpudding.android_back_stage.common.JwtUtil;
import com.catpudding.android_back_stage.common.Result;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.model.ChangePasswordModel;
import com.catpudding.android_back_stage.system.model.UserIdModel;
import com.catpudding.android_back_stage.system.model.UserNameModel;
import com.catpudding.android_back_stage.system.service.UserService;
import com.catpudding.android_back_stage.system.vo.LoginResult;
import com.catpudding.android_back_stage.system.vo.UserBillInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/change_username",method = RequestMethod.POST)
    public Result<?> changeUserName(
            @RequestHeader("X-Access-Token") String token,
            @RequestBody UserNameModel userNameModel
            ){
        Result<LoginResult> result = new Result<>();

        SysUser user = JwtUtil.parse(token).getSysUser();
        user.setUsername(userNameModel.getUsername());
        if(!userNameModel.getUsername().equals("")){
            try {
                result.setResult(userService.changeUsername(user));
                result.setSuccess(true);
                result.setCode(200);
            } catch (Exception e) {
                result.setCode(10001);
                result.setSuccess(false);
                result.setMessage(e.getMessage());
            }
        }else {
            result.setCode(10001);
            result.setSuccess(false);
            result.setMessage("用户名不能为空！");
        }
        return result;
    }

    @RequestMapping(value = "/change_password",method = RequestMethod.POST)
    public Result<?> changePassword(
            @RequestHeader("X-Access-Token") String token,
            @RequestBody ChangePasswordModel changePasswordModel
    ){
        Result<LoginResult> result = new Result<>();
        SysUser user = JwtUtil.parse(token).getSysUser();
        if(!changePasswordModel.getNewPassword().equals("") && !changePasswordModel.getOldPassword().equals("")){
            try {
                result.setResult(userService.changePassword(user, changePasswordModel.getOldPassword(), changePasswordModel.getNewPassword()));
                result.setSuccess(true);
                result.setCode(200);
            } catch (Exception e) {
                result.setCode(10001);
                result.setSuccess(false);
                result.setMessage(e.getMessage());
            }
        }else {
            result.setCode(10001);
            result.setSuccess(false);
            result.setMessage("密码不能为空！");
        }
        return result;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public Result<?> getAllUser(
            @RequestHeader("X-Access-Token") String token
    ){
        Result<List<SysUser>> result = new Result<>();
        if(JwtUtil.checkIsAdmin(token)){
            result.setResult(userService.getAllUser());
            result.setSuccess(true);
            result.setCode(200);
        }else{
            result.setSuccess(false);
            result.setMessage("您无权进行测操作");
            result.setCode(10001);
        }
        return result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Result<?> deleteAllUser(
            @RequestHeader("X-Access-Token") String token,
            @RequestBody UserIdModel idModel
    ){
        Result<String> result = new Result<>();
        if(JwtUtil.checkIsAdmin(token)){
            if(userService.delete(idModel.getUserId())){
                result.setMessage("删除成功");
                result.setSuccess(true);
                result.setCode(200);
            }else{
                result.setSuccess(false);
                result.setMessage("删除失败！");
                result.setCode(10001);
            }
        }else{
            result.setSuccess(false);
            result.setMessage("您无权进行测操作");
            result.setCode(10001);
        }
        return result;
    }

    @RequestMapping(value = "/user_bill_info",method = RequestMethod.GET)
    public Result<?> getUserBillInfo(
            @RequestHeader("X-Access-Token") String token
    ){
        Result<UserBillInfo> result = new Result<>();

        SysUser user = JwtUtil.parse(token).getSysUser();
        try {
            UserBillInfo userBillInfo = userService.getUserBillInfo(user.getId());
            result.setResult(userBillInfo);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setCode(10001);
        }
        return result;
    }

}
