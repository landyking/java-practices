package com.github.landyking.learnShiro.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author: landy
 * @date: 2017-11-07 22:20
 */
@RestController
@RequestMapping("/admin/")
public class AdminController {
    @RequestMapping("login")
    public Object login(String name) {
        if (StringUtils.isEmpty(name)) {
            name = "DefaultName";
        }
        MyToken token = new MyToken();
        token.setUsername(name);
        token.setLoginType(MyToken.LoginType.admin);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return Collections.singletonMap("jstoken", subject.getSession().getId().toString());
    }

    @RequestMapping("show")
    public Object show() {
        Subject subject = SecurityUtils.getSubject();
        HashMap<String, Object> rst = new HashMap<String, Object>();
        MyUser user = (MyUser) subject.getPrincipal();
        rst.put("name", user.getName());
        rst.put("type", user.getType());
        return rst;
    }

}
