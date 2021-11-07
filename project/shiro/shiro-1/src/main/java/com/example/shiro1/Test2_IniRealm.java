package com.example.shiro1;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;


public class Test2_IniRealm {

    @Test
    public void testIniRealm() {

        /**
         * resources目录下新建user.ini文件
         * user.ini配置
         *   [users]
         *   username01=pwd01
         *
         * 读取ini文件中的用户配置
         */
        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(dsm);
        Subject subject = SecurityUtils.getSubject();

        // 认证通过
        UsernamePasswordToken token = new UsernamePasswordToken("username01", "pwd01");
        //认证失败，抛出异常
        //UsernamePasswordToken token = new UsernamePasswordToken("username012", "pwd01");

        subject.login(token);
        System.out.println("是否已经认证：" + subject.isAuthenticated());

    }


    @Test
    public void testIniRealmRole() {

        /**
         * resources目录下新建user.ini文件
         * user.ini配置
         *   [users]
         *   username01=pwd01,admin
         *   [roles]
         *   admin=user:delete,user:update
         *
         * 读取ini文件中的用户配置
         */
        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(dsm);
        Subject subject = SecurityUtils.getSubject();

        // 认证通过
        UsernamePasswordToken token = new UsernamePasswordToken("username01", "pwd01");
        subject.login(token);

        // 检查角色
        subject.checkRole("admin");
        // 检查权限
        subject.checkPermission("user:update");

        // 检查权限，没有此权限，抛出异常UnauthorizedException
        //subject.checkPermission("user:add");
    }


}
