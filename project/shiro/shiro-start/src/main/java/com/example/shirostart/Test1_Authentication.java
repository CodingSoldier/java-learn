package com.example.shirostart;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

/**
 * Authentication测试
 */
public class Test1_Authentication {

    /**
     * 1.2中文文档
     * https://waylau.gitbooks.io/apache-shiro-1-2-x-reference/content/
     */
    @Test
    public void testAuthentication(){

        SimpleAccountRealm sar = new SimpleAccountRealm();
        sar.addAccount("username01", "pwd01");

        // 1、构建SecurityManager环境
        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(sar);

        // 2、主体提交认证请求
        SecurityUtils.setSecurityManager(dsm);
        // 一个 primary principal 是在整个应用程序中唯一标识 Subject 的 principal
        Subject subject = SecurityUtils.getSubject();

        // 认证通过
        UsernamePasswordToken token = new UsernamePasswordToken("username01", "pwd01");
        // 认证失败，抛出异常
        //UsernamePasswordToken token = new UsernamePasswordToken("username", "pwd01");

        /**
         * login源码解析
         * 调用securityManager.login()
         * securityManager.login() 调用 Authenticator的authenticate()方法
         * Authenticator的authenticate()通过Realm认证用户
         */
        subject.login(token);

        System.out.println("是否已经认证："+subject.isAuthenticated());

        // 退出后，认证失败
        subject.logout();

        System.out.println("是否已经认证："+subject.isAuthenticated());
    }


    /**
     * 检查角色
     */
    @Test
    public void testRoles(){

        SimpleAccountRealm sar = new SimpleAccountRealm();
        //sar.addAccount("username01", "pwd01", "admin", "user1");
        sar.addAccount("username01", "pwd01", "admin");

        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(sar);

        SecurityUtils.setSecurityManager(dsm);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("username01", "pwd01");

        subject.login(token);

        /**
         * 检验角色
         *
         * 源码
         * 通过securityManager校验角色
         * securityManager通过Authorizer校验角色
         * Authorizer遍历角色集合
         */
        subject.checkRoles("admin");
        //subject.checkRoles("admin", "user");
    }





}
