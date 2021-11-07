package com.example.shirojwt;

import com.example.shirojwt.common.Constant;
import com.example.shirojwt.common.CustomAuthenticationException;
import com.example.shirojwt.model.Permission;
import com.example.shirojwt.model.Role;
import com.example.shirojwt.model.User;
import com.example.shirojwt.service.UserService;
import com.example.shirojwt.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private UserService userService;

    /**
     * 最好保持单例
     * JwtRealm可以不交给spring管理，在创建JwtRealm的时候需要创建者传递参数UserService
     */
    public JwtRealm(UserService userService) {
        this.userService = userService;

        /**
         * 启动认证缓存，默认是false。源码如下
         * org.apache.shiro.realm.AuthenticatingRealm#AuthenticatingRealm()
         *     this.authenticationCachingEnabled = false;
         */
        this.setAuthenticationCachingEnabled(true);

        /**
         * 启动授权缓存，默认就是true，代码如下
         * org.apache.shiro.realm.AuthorizingRealm#AuthorizingRealm()
         *     this.authorizationCachingEnabled = true;
         */
        // this.setAuthorizationCachingEnabled(true);

        // 设置缓存管理器，使用shiro自带的MemoryConstrainedCacheManager即可
        this.setCacheManager(new MemoryConstrainedCacheManager());

    }

    // subject.login(token)方法中的token是JwtToken时，调用此Realm的doGetAuthenticationInfo
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证用户
     * 本方法被 org.apache.shiro.realm.AuthenticatingRealm#getAuthenticationInfo()调用
     * AuthenticationInfo info = this.getCachedAuthenticationInfo(token);
     * 如果通过token在缓存中获取到用户认证，就不调用本方法
     * <p>
     * 补充一点：用户认证接口往往只传递principals信息，不传credentials。在spring security中也是这种思路
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws CustomAuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        if (StringUtils.isEmpty(token)) {
            throw new CustomAuthenticationException("token为空");
        }

        // 这里还判断下用户是否存在是防止客户端没调用登陆接口，就直接伪造一个token
        String username = JWTUtil.getUsername(token);
        User user = userService.getUser(username);
        if (user == null) {
            throw new CustomAuthenticationException("无此用户");
        }
        /**
         * token无效，抛出异常
         * MyControllerAdvice捕获MyException异常后，将Constant.CODE_TOKEN_ERROR返回给前端，前端收到此code后跳转登录页
         */
        if (!JWTUtil.verify(token, username, user.getPassword())) {
            throw new CustomAuthenticationException(Constant.CODE_TOKEN_ERROR, "token无效，请重新登录");
        }
        if (JWTUtil.isExpired(token)) {
            log.error("token过期，抛出异常信息【token无效，请重新登录】");
            // 不抛出太详细的异常信息给前端，避免黑客攻击
            throw new CustomAuthenticationException(Constant.CODE_TOKEN_ERROR, "token无效，请重新登录");
        }
        return new SimpleAuthenticationInfo(token, Constant.CREDENTIALS_EMPTY, this.getName());
    }

    /**
     * 用户授权
     * 本方法被 org.apache.shiro.realm.AuthorizingRealm#getAuthorizationInfo() 调用
     * Cache<Object, AuthorizationInfo> cache = this.getAvailableAuthorizationCache();
     * 如果获取到缓存，就通过cache.get(key)获取授权数据，key就是principal
     * <p>
     * 若无缓存，在调用了本方法后，会将本方法返回的AuthorizationInfo添加到缓存中
     * cache.put(key, info);
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = JWTUtil.getUsername(principalCollection.getPrimaryPrincipal().toString());
        User user = userService.getUser(username);

        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();

        List<String> roleList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getRoleList())) {
            for (Role role : user.getRoleList()) {
                roleList.add(role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissionList())) {
                    for (Permission permission : role.getPermissionList()) {
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
