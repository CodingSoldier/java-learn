package com.example.shirojwt;

import com.example.shirojwt.common.Constant;
import com.example.shirojwt.common.CustomException;
import com.example.shirojwt.model.Permission;
import com.example.shirojwt.model.Role;
import com.example.shirojwt.model.User;
import com.example.shirojwt.service.UserService;
import com.example.shirojwt.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
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

    // 模式UserService获取数据库数据
    private UserService userService;

    // 最好保持单例
    public JwtRealm(UserService userService) {
        this.userService = userService;

        // 启动认证缓存，默认是false
        this.setAuthenticationCachingEnabled(true);
        // 启动授权缓存，默认是true
        // this.setAuthorizationCachingEnabled(true);
        // 设置缓存
        this.setCacheManager(new MemoryConstrainedCacheManager());

    }

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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken){
        String token = (String) authenticationToken.getPrincipal();
        if (StringUtils.isEmpty(token)){
            throw new CustomException("token为空");
        }
        String username = JWTUtil.getUsername(token);
        User user = userService.getUser(username);
        if (user == null){
            throw new CustomException("无此用户");
        }
        /**
         * token无效，抛出异常
         * MyControllerAdvice捕获MyException异常后，将Constant.CODE_TOKEN_ERROR返回给前端，前端收到此code后跳转登录页
         */
        if (!JWTUtil.verify(token, username, user.getPassword())){
            throw new CustomException(Constant.CODE_TOKEN_ERROR, "token无效，请重新登录");
        }
        if (JWTUtil.isExpired(token)){
            throw new CustomException(Constant.CODE_TOKEN_ERROR, "token无效，请重新登录");
        }
        return new SimpleAuthenticationInfo(token, Constant.CREDENTIALS_EMPTY, this.getName());
    }

    /**
     * 用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.getPrimaryPrincipal().toString());
        User user = userService.getUser(username);

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
