package com.catpudding.android_back_stage.system.controller;

import com.catpudding.android_back_stage.common.Result;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.model.SysLoginModel;
import com.catpudding.android_back_stage.system.model.SysRegisteModel;
import com.catpudding.android_back_stage.system.service.UserService;
import com.catpudding.android_back_stage.system.vo.LoginResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param registeModel
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result<?> register(@RequestBody SysRegisteModel registeModel){
        Result<String> result = new Result<>();
        SysUser sysUser = new SysUser();
        sysUser.setUsername(registeModel.getUsername());
        sysUser.setPassword(registeModel.getPassword());
        sysUser.setPhone(registeModel.getPhone());
        try {
            int row = userService.registeUser(sysUser);
            if(row > 0){
                result.setCode(200);
                result.setSuccess(true);
                result.setMessage("注册成功");
            }else{
                result.setCode(10002);
                result.setSuccess(false);
                result.setMessage("登录失败");
            }

        } catch (Exception e) {
            result.setCode(10001);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result<?> login(@RequestBody SysLoginModel loginModel){
        Result<LoginResult> result = new Result<>();
        LoginResult loginResult = null;
        try {
            loginResult = userService.login(loginModel.getUsername(),loginModel.getPassword());
            result.setResult(loginResult);
            result.setCode(200);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setCode(10001);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
