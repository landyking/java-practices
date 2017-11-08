package com.github.landyking.learnShiro.web;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: landy
 * @date: 2017-11-07 22:41
 */
public class TypeAccessControlFilter extends AccessControlFilter implements InitializingBean {
    private String type;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(getType());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated()) {
            MyUser user = (MyUser) subject.getPrincipal();
            if (user.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "无权访问");
        return false;
    }
}
