package com.example.shirojwt;


import com.example.shirojwt.filter.CustomPermissionsAuthorizationFilter;
import com.example.shirojwt.filter.JwtFilter;
import com.example.shirojwt.service.UserService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

/**
 * @author chenpiqian
 * @date: 2020-01-15
 */
@Configuration
public class ShiroConfig {

    // 使用@Lazy避免UserService为空
    @Lazy
    @Autowired
    UserService userService;

    // 创建jwtRealm
    @Bean
    public JwtRealm jwtRealm(){
        return new JwtRealm(userService);
    }

    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("jwtRealm") JwtRealm jwtRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 设置realm
        manager.setRealm(jwtRealm);

        /**
         * 禁止session持久化存储
         * 一定要禁止session持久化。不然清除认证缓存、授权缓存后，shiro依旧能从session中读取到认证信息
         */
        //DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        //DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        //defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        //subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        //manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        //添加filter，factoryBean.getFilters()获取到的是引用，可直接添加值
        factoryBean.getFilters().put("jwt", new JwtFilter());
        factoryBean.getFilters().put("customPerms", new CustomPermissionsAuthorizationFilter());

        // factoryBean.getFilterChainDefinitionMap();默认是size=0的map
        Map<String, String> definitionMap = factoryBean.getFilterChainDefinitionMap();

        /**
         * put的顺序很重要，当/open/**匹配到请求url后，后面的匹配规则/**不起作用
         * 官方将这种原则称为 FIRST MATCH WINS
         * https://waylau.gitbooks.io/apache-shiro-1-2-x-reference/content/III.%20Web%20Applications/10.%20Web.html
         *
         */
        definitionMap.put("/open/**", "anon");

        // 需要权限的url会经过两个过滤器jwt、customPerms
        definitionMap.put("/user/delete", "jwt, customPerms["+userService.getUserDelete().getName()+"]");
        definitionMap.put("/user/edit", "jwt, customPerms["+userService.getUserEdit().getName()+"]");
        definitionMap.put("/user/add", "jwt, customPerms["+userService.getUserAdd().getName()+"]");
        definitionMap.put("/user/view", "jwt, customPerms["+userService.getUserView().getName()+"]");
        definitionMap.put("/test/other", "jwt, customPerms["+userService.getTestOther().getName()+"]");
        definitionMap.put("/role/permission/edit", "jwt, customPerms["+userService.getRolePermisssionEdit().getName()+"]");

        definitionMap.put("/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(definitionMap);
        return factoryBean;
    }


}
