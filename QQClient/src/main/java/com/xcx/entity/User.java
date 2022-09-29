package com.xcx.entity;

import java.io.Serializable;

/**
 * @Author: xcx
 * @Since: 2022/9/29
 */
public class User implements Serializable {

    private final static long serialVersionUID = 1001111L;//以便序列化
    private String userId;//用户id
    private String pwd;//用户密码

    public User(){}
    public User(String userId, String pwd){
        this.userId = userId;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
