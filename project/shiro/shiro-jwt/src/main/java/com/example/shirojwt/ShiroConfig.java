package com.example.shirojwt;


import com.example.shirojwt.filter.CustomPermissionsAuthorizationFilter;
import com.example.shirojwt.filter.JwtFilter;
import com.example.shirojwt.service.UserService;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
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

        /**
         * factoryBean.getFilterChainDefinitionMap();默认是size=0的LinkedHashMap
         */
        Map<String, String> definitionMap = factoryBean.getFilterChainDefinitionMap();

        /**
         * definitionMap是一个LinkedHashMap，是一个链表结构
         * put的顺序很重要，当/open/**匹配到请求url后，将不再检查后面的规则
         * 官方将这种原则称为 FIRST MATCH WINS
         * https://waylau.gitbooks.io/apache-shiro-1-2-x-reference/content/III.%20Web%20Applications/10.%20Web.html
         */
        definitionMap.put("/open/**", "anon");

        /**
         * 需要权限的url会经过两个过滤器jwt、customPerms
         * 实际项目中可以将这些接口权限规则放到数据库中去
         */
        definitionMap.put("/user/delete", "jwt, customPerms["+userService.getUserDelete().getName()+"]");
        definitionMap.put("/user/edit", "jwt, customPerms["+userService.getUserEdit().getName()+"]");
        definitionMap.put("/user/add", "jwt, customPerms["+userService.getUserAdd().getName()+"]");
        definitionMap.put("/user/view", "jwt, customPerms["+userService.getUserView().getName()+"]");
        definitionMap.put("/test/other", "jwt, customPerms["+userService.getTestOther().getName()+"]");
        definitionMap.put("/role/permission/edit", "jwt, customPerms["+userService.getRolePermisssionEdit().getName()+"]");

        // 前面的规则都没匹配到，最后添加一条规则，所有的接口都要经过com.example.shirojwt.filter.JwtFilter这个过滤器验证
        definitionMap.put("/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(definitionMap);

        return factoryBean;
    }


}
