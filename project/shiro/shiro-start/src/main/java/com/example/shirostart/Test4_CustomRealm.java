package com.example.shirostart;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

/**
 * 使用自定义realm
 */
public class Test4_CustomRealm {

    @Test
    public void testCustomRealm(){

        CustomRealm customRealm = new CustomRealm();
        // 配置CustomRealm使用md5加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        //md5加密次数
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(customRealm);

        SecurityUtils.setSecurityManager(dsm);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(CustomRealm.USER_NAME, CustomRealm.PWD);

        subject.login(token);

        System.out.println("是否已认证："+subject.isAuthenticated());

        subject.checkRoles(CustomRealm.ROLE_ADMIN);
        // 自定义Realm不需要手动开启权限校验
        subject.checkPermission(CustomRealm.PERMISSIONS_DELETE);

    }
}
