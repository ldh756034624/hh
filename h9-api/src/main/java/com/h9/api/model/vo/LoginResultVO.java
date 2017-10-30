package com.h9.api.model.vo;

import com.h9.common.db.entity.User;

/**
 * Created by itservice on 2017/10/28.
 */
public class LoginResultVO {
    private Long id;
    private String token;
    private String imgUrl;
    private String nickName;

    public static LoginResultVO convert(User user,String token){
        LoginResultVO vo = new LoginResultVO();
        vo.setId(user.getId());
        vo.setToken(token);
        vo.setImgUrl(user.getAvatar());
        vo.setNickName(user.getNickName());
        return vo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}