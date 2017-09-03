package com.github.landyking.learnShiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tutorial2 {

    private static final transient Logger log = LoggerFactory.getLogger(Tutorial2.class);

    public static void main(String[] args) {

        log.info("My First Apache Shiro Application");

        //1.
        org.apache.shiro.util.Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        //2.
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        //3.
        SecurityUtils.setSecurityManager(securityManager);

        System.exit(0);
    }

}