package com.cpq.shiro2.config;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm
 */
public class CustomRealm extends AuthorizingRealm {

    public static String USER_NAME = "username01";
    public static String PWD = "123456";
    public static String ROLE_ADMIN = "admin";
    public static String PERMISSIONS_DELETE = "user:delete";

    /**
     * AuthenticationInfo接口存储登陆信息，用于登陆认证
     * AuthorizationInfo接口存储角色、权限信息，用于权限校验
     */

    /**
     * 认证方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 1、通过AuthenticationToken获取用户名
        String username = String.valueOf(authenticationToken.getPrincipal());

        // 2、通过用户名去数据库拿密码，数据库的密码已经使用md5加密了
        //String password = new Md5Hash(PWD).toString();
        // md5加盐
        String salt = "salt123";
        String password = new Md5Hash(PWD, salt).toString();

        if (StringUtils.isEmpty(password)){
            return null;
        }

        //这名字随意
        String realmName = "crn";
        SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(username, password, realmName);
        // SimpleAuthenticationInfo设置盐
        sai.setCredentialsSalt(ByteSource.Util.bytes(salt));

        return sai;
    }

    /**
     * 授权方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //1、通过PrincipalCollection获取用户名
        String userName = String.valueOf(principalCollection.getPrimaryPrincipal());

        //2、通过用户名获取角色、权限
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        roles.add(ROLE_ADMIN);
        permissions.add(PERMISSIONS_DELETE);

        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        sai.setRoles(roles);
        sai.setStringPermissions(permissions);

        return sai;
    }

}
