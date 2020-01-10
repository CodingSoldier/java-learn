package com.cpq.shiro2.config;

import com.cpq.shiro2.user.entity.Permission;
import com.cpq.shiro2.user.entity.Role;
import com.cpq.shiro2.user.entity.UserVo;
import com.cpq.shiro2.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    /**
     * 通过用户名查找用户
     * authenticationToken.getPrincipal() 是用户名
     * authenticationToken.getCredentials() 是密码
     *
     * SimpleAuthenticationInfo(Object principal, Object credentials, String realmName)中的principal可以是一个类对象
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        UserVo userVo = userService.findByUsername(username);
        return new SimpleAuthenticationInfo(userVo, userVo.getPassword(), this.getClass().getName());
    }

    /**
     * 通过 principalCollection 获取角色、权限
     * 并将角色权限set到SimpleAuthorizationInfo中
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserVo userVo = (UserVo) principals.fromRealm(this.getClass().getName()).iterator().next();
        List<String> permissionList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        Set<Role> roleSet = userVo.getRoles();
        if (CollectionUtils.isNotEmpty(roleSet)) {
            for(Role role : roleSet) {
                roleNameList.add(role.getRname());
                Set<Permission> permissionSet = role.getPermissions();
                if (CollectionUtils.isNotEmpty(permissionSet)) {
                    for (Permission permission : permissionSet) {
                        permissionList.add(permission.getName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        info.addRoles(roleNameList);
        return info;
    }

}
