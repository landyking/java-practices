package com.github.landyking.learnShiro.kaitao3;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/9/21 9:46
 * note:
 */
public class MyRolePermissionResolver implements RolePermissionResolver {
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        if ("role1".equals(roleString)) {
            return Arrays.asList((Permission) new WildcardPermission("menu:*"));
        }
        return null;
    }
}