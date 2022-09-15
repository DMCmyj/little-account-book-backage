package com.catpudding.android_back_stage.common;


import com.alibaba.fastjson.JSON;
import com.catpudding.android_back_stage.system.entity.SysUser;
import com.catpudding.android_back_stage.system.entity.SysUserRole;
import com.catpudding.android_back_stage.system.entity.UserGetByToken;
import io.jsonwebtoken.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author lw
 * @since 2021/12/29
 */
public class JwtUtil {

    private static final String secretKey="catpudding@184";

    /**
     * 生成 JWT TOKEN
     * @param user 用户信息
     * @return String token
     */
    public static String createToken(SysUser user, List<SysUserRole> roleList){
        JwtBuilder jwtBuilder = Jwts.builder();
//        String roles = "";
//        if(!roleList.isEmpty()){
//            roles = roleList.get(0).getRoleName();
//            for(int i = 1;i< roleList.size();i++){
//                roles += "," + roleList.get(i).getRoleName();
//            }
//        }
        String token = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("userId", user.getId())
                .claim("userName", user.getUsername())
                .claim("userRole",roleList)
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

    /**
     * 解析 JWT TOKEN
     * @param token JWT TOKEN
     * @return User user
     */
    public static UserGetByToken parse(String token){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(secretKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        UserGetByToken user = new UserGetByToken();
        user.setSysUser(new SysUser());
        user.setRoles(new ArrayList<>());
        user.getSysUser().setId(claims.get("userId").toString());
        user.getSysUser().setUsername((String) claims.get("username"));
        user.setRoles((List<SysUserRole>) claims.get("userRole"));
        return user;
    }

    /**
     * 判断用户是否是管理员
     * @param token
     * @return
     */
    public static Boolean checkIsAdmin(String token){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(secretKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        UserGetByToken user = new UserGetByToken();
        user.setSysUser(new SysUser());
        user.setRoles(new ArrayList<>());
        String s =  JSON.toJSONString( JSON.toJSON((List<SysUserRole>) claims.get("userRole")));
        List<SysUserRole> roleList = JSON.parseArray(s,SysUserRole.class);
        boolean flag = false;
        for (int i = 0;i<roleList.size();i++){
            SysUserRole role = (SysUserRole) roleList.get(i);

            if(role.getRoleName().equals("管理员")){
                flag = true;
                break;
            }
        }
        return flag;
    }

}
