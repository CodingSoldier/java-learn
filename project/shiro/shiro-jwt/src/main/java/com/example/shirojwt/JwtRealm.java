package com.example.shirojwt;

import com.example.shirojwt.model.Permission;
import com.example.shirojwt.model.Role;
import com.example.shirojwt.model.User;
import com.example.shirojwt.model.UserFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenpiqian
 * @date: 2020-01-15
 */
public class JwtRealm extends AuthorizingRealm {

    // subject.login(token)方法中的token是JwtToken时，调用此Realm的doGetAuthenticationInfo
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证用户
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        if (StringUtils.isEmpty(token)){
            throw new AuthenticationException("token为空");
        }
        String username = JWTUtil.getUsername(token);
        User user = UserFactory.getUser(username);
        if (user == null){
            throw new AuthenticationException("无此用户");
        }
        if (JWTUtil.verify(token, username, user.getPassword())){
            throw new AuthenticationException("非法token");
        }
        if (JWTUtil.isExpired(token)){
            throw new AuthenticationException("token过期");
        }
        return new SimpleAuthenticationInfo(token, user.getPassword(), this.getName());
    }

    /**
     * 用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.getPrimaryPrincipal().toString());
        User user = UserFactory.getUser(username);

        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();

        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getRoleList())){
            for (Role role:user.getRoleList()){
                roleList.add(role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissionList())){
                    for (Permission permission:role.getPermissionList()){
                        permissionList.add(permission.getName());
                    }
                }
            }
        }
        sai.addRoles(roleList);
        sai.addStringPermissions(permissionList);

        return sai;
    }
}
