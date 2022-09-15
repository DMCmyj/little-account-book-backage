package com.catpudding.android_back_stage.system.model;

import lombok.Data;

@Data
public class ChangePasswordModel {
    private String oldPassword;
    private String newPassword;
}
