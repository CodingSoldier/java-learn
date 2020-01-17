package com.example.shirojwt.controller;

import com.example.shirojwt.JwtRealm;
import com.example.shirojwt.JwtToken;
import com.example.shirojwt.common.CustomException;
import com.example.shirojwt.common.Result;
import com.example.shirojwt.model.User;
import com.example.shirojwt.service.UserService;
import com.example.shirojwt.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@RestController
@Slf4j
@RequestMapping("/open")
public class OpenApiCtrl {

    @Autowired
    ShiroFilterFactoryBean shiroFilterFactoryBean;
    @Autowired
    JwtRealm jwtRealm;

    @Autowired
    UserService userService;

    /**
     * 登陆接口
     * 用户名             密码
     * admin           admin-pwd
     * cudrOtherUser   cudrOtherUser-pwd
     * viewUser        viewUser-pwd
     */
    @PostMapping("/login")
    public Result openLogin(@RequestBody User userVo){
        String username = userVo.getUsername();
        String password = userVo.getPassword();

        User user = userService.getUser(username);
        if (user == null){
            throw new CustomException("无此用户");
        }
        if (!user.getPassword().equals(new Md5Hash(password).toString())){
            throw new CustomException("用户名或密码错误");
        }

        String authorization = JWTUtil.sign(username, user.getPassword());

        JwtToken jwtToken = new JwtToken(authorization);
        Subject subject = SecurityUtils.getSubject();
        subject.login(jwtToken);

        return Result.success(authorization);
    }

    /**
     * 测试动态修改url安全配置
     */
    @GetMapping("/definition/test")
    public Result test1() throws Exception{

        AbstractShiroFilter shiroFilter = (AbstractShiroFilter)shiroFilterFactoryBean.getObject();
        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
        // 清空老的权限控制
        manager.getFilterChains().clear();
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();


        // 生成新的definitionMap
        LinkedHashMap<String, String> definitionMap = new LinkedHashMap<>();
        definitionMap.put("/open/**", "anon");
        /**
         * 把这些url权限校验取消掉
         * definitionMap.put("/**", "jwt") 将匹配到user的请求url，只要登陆就能访问
         */
        //definitionMap.put("/user/delete", "jwt, customPerms["+userService.getUserDelete().getName()+"]");
        //definitionMap.put("/user/edit", "jwt, customPerms["+userService.getUserEdit().getName()+"]");
        //definitionMap.put("/user/add", "jwt, customPerms["+userService.getUserAdd().getName()+"]");
        //definitionMap.put("/user/view", "jwt, customPerms["+userService.getUserView().getName()+"]");
        definitionMap.put("/test/other", "jwt, customPerms["+userService.getTestOther().getName()+"]");
        definitionMap.put("/role/permission/edit", "jwt, customPerms["+userService.getRolePermisssionEdit().getName()+"]");
        definitionMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(definitionMap);



        // 重新构建生成权限过滤链
        for (Map.Entry<String, String> entry : definitionMap.entrySet()) {
            String url = entry.getKey();
            String chainDefinition = entry.getValue();
            manager.createChain(url, chainDefinition);
        }

        return Result.success("test");
    }

    /**
     * 测试清除jwtRealm缓存
     */
    @GetMapping("/cache/test")
    public Result test2(){
        Cache<Object, AuthenticationInfo> authen = jwtRealm.getAuthenticationCache();
        Cache<Object, AuthorizationInfo> author = jwtRealm.getAuthorizationCache();
        log.info("当前缓存, 认证缓存 = {} 授权缓存 = {}", authen, author);

        authen.clear();
        author.clear();
        log.info("缓存清除完成，认证缓存size = {} 授权缓存size = {}", authen.size(), author.size());

        return Result.success("test");
    }


}
