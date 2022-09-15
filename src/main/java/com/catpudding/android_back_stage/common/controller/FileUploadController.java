package com.catpudding.android_back_stage.common.controller;

import com.catpudding.android_back_stage.common.JwtUtil;
import com.catpudding.android_back_stage.common.Result;
import com.catpudding.android_back_stage.common.UUIDGenerator;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/upload")
public class FileUploadController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final String SAVE_PATH = "src/main/resources/static/";
    private final String GET_PATH = "http://www.catpudding.cn/resources/static/";

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/user_avatar", method = RequestMethod.POST)
    public Result<?> uploadUserAvatar(MultipartFile uploadFile, HttpServletRequest req,
                                      @RequestHeader("X-Access-Token") String token) {
        Result<String> result = new Result<>();

        //创建文件夹对象
        String format = sdf.format(new Date());
        String subPath = "img/user_avatar/" + format;
        File folder = new File(SAVE_PATH + subPath);
        String savePath = "";

        try {
            savePath = folder.getCanonicalPath();
            System.out.println(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = "";
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = uploadFile.getOriginalFilename();//获取文件原名
        String newName = UUIDGenerator.generate() +
                oldName.substring(oldName.lastIndexOf("."), oldName.length());//保存文件类型
        try {
            uploadFile.transferTo(new File(savePath + "/" + newName));
            filePath = GET_PATH + subPath + "/" + newName;
            result.setResult(filePath);
            SysUser sysUser = JwtUtil.parse(token).getSysUser();
            sysUser.setAvatar(filePath);
            if(userService.updateById(sysUser)){
                result.setMessage("上传成功！");
            }else {
                result.setMessage("修改头像失败！");
                result.setCode(10002);
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setMessage("上传失败！");
            result.setCode(10001);
            result.setSuccess(false);
        }

        return result;
    }
}
