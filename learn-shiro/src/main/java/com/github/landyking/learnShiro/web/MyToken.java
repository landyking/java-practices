package com.github.landyking.learnShiro.web;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author: landy
 * @date: 2017-11-07 22:23
 */
public class MyToken extends UsernamePasswordToken {
    enum LoginType {
        api, admin
    }

    private LoginType loginType;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
