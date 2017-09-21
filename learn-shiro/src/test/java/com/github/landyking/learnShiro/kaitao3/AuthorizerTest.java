package com.github.landyking.learnShiro.kaitao3;

import com.github.landyking.learnShiro.kaitao.AbstractKaitaoTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/9/21 9:44
 * note:
 */
public class AuthorizerTest extends AbstractKaitaoTest {

    @Test
    public void testIsPermitted() {
        login("classpath:kaitao3/shiro-authorizer.ini", "zhang", "123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }
}
