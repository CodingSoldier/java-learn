package com.example.shiro1;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;


public class Test3_JDBCRealm {

    DruidDataSource source = new DruidDataSource();

    {
        source.setUrl("jdbc:mysql://localhost:3306/shiro?useUnicode=true&useSSL=false&characterEncoding=utf8");
        source.setUsername("root");
        source.setPassword("cpq..123");
    }

    /**
     * JdbcRealm默认使用的权限表
     * <p>
     * 新建表users表
     * CREATE TABLE `users` (
     * `id` int(11) NOT NULL,
     * `username` varchar(255) DEFAULT NULL,
     * `password` varchar(255) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     * <p>
     * INSERT INTO `shiro`.`users`(`id`, `username`, `password`) VALUES (1, 'username01', 'pwd01');
     * <p>
     * 新建角色表
     * CREATE TABLE `user_roles` (
     * `id` int(11) NOT NULL,
     * `username` varchar(255) DEFAULT NULL,
     * `role_name` varchar(255) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     * <p>
     * INSERT INTO `shiro`.`user_roles`(`id`, `username`, `role_name`) VALUES (1, 'username01', 'admin');
     * <p>
     * 新建权限表
     * CREATE TABLE `roles_permissions` (
     * `id` int(11) NOT NULL,
     * `role_name` varchar(255) DEFAULT NULL,
     * `permission` varchar(255) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     * <p>
     * INSERT INTO `shiro`.`roles_permissions`(`id`, `role_name`, `permission`) VALUES (1, 'admin', 'user:select');
     */
    @Test
    public void testJdbcRealmDefault() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(source);
        // 开启权限，默认为false
        jdbcRealm.setPermissionsLookupEnabled(true);

        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(dsm);
        Subject subject = SecurityUtils.getSubject();

        // 认证失败，抛出异常
        UsernamePasswordToken token = new UsernamePasswordToken("username", "pwd");

        subject.login(token);

        System.out.println("是否已经认证：" + subject.isAuthenticated());

        subject.checkRoles("admin");

        // 必须先开启权限开关
        subject.checkPermission("user:select");

    }


    /**
     * 使用自定义表、自定义SQL认证用户
     * <p>
     * CREATE TABLE `t_users` (
     * `id` int(11) NOT NULL,
     * `account` varchar(255) DEFAULT NULL,
     * `pwd` varchar(255) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     * <p>
     * INSERT INTO `shiro`.`t_users`(`id`, `account`, `pwd`) VALUES (1, 'account01', '123456');
     * <p>
     * CREATE TABLE `t_role` (
     * `id` int(11) NOT NULL,
     * `account_name` varchar(255) DEFAULT NULL,
     * `role_name` varchar(255) DEFAULT NULL,
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
     * <p>
     * INSERT INTO `shiro`.`t_role`(`id`, `account_name`, `role_name`) VALUES (1, 'account01', 'user');
     */
    @Test
    public void testJdbcRealmCustom() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(source);
        jdbcRealm.setPermissionsLookupEnabled(true);

        // 使用自定义表，自定义SQL，查询用户
        String userSql = "SELECT pwd FROM t_users WHERE account=?";
        jdbcRealm.setAuthenticationQuery(userSql);

        // 查询角色
        String roleSql = "SELECT role_name FROM t_role WHERE account_name=?";
        jdbcRealm.setUserRolesQuery(roleSql);

        DefaultSecurityManager dsm = new DefaultSecurityManager();
        dsm.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(dsm);
        Subject subject = SecurityUtils.getSubject();

        // 认证失败，抛出异常
        UsernamePasswordToken token = new UsernamePasswordToken("account01", "123456");

        subject.login(token);

        System.out.println("是否已经认证：" + subject.isAuthenticated());

        subject.checkRoles("user");

    }


}
