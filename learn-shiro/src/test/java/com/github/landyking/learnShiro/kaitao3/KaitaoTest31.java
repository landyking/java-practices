package com.github.landyking.learnShiro.kaitao3;

import com.github.landyking.learnShiro.kaitao.AbstractKaitaoTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/9/21 9:21
 * note:
 */
public class KaitaoTest31 extends AbstractKaitaoTest{
    @Test
    public void testHasRole() {
        login("classpath:kaitao3/shiro-role-31.ini", "zhang", "123");
        //判断拥有角色：role1
        Assert.assertTrue(subject().hasRole("role1"));
        //判断拥有角色：role1 and role2
        Assert.assertTrue(subject().hasAllRoles(Arrays.asList("role1", "role2")));
        //判断拥有角色：role1 and role2 and !role3
        boolean[] result = subject().hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertEquals(true, result[0]);
        Assert.assertEquals(true, result[1]);
        Assert.assertEquals(false, result[2]);
    }
}
