package com.catpudding.android_back_stage.system.vo;

import lombok.Data;

@Data
public class LoginResult {
    private String id;
    private String username;
    private String avatar;
    private Integer sex;
    private String email;
    private String phone;
    private String realname;
    private String token;
    private Boolean isAdmin;
}
