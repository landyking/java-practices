package com.github.landyking.learnShiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author: landy
 * @date: 2017-09-03 18:09
 */
public class MainTest {
    @Test
    public void test() throws Exception {
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject);
    }
}