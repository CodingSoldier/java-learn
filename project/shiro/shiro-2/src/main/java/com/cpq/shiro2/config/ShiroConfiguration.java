package com.cpq.shiro2.config;

import com.cpq.shiro2.filter.RoleAnyFilter;
import com.cpq.shiro2.session.CustomSessionManage;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Autowired
    CustomSessionManage customSessionManage;
    //@Autowired
    //RedisCacheManager redisCacheManager;

    /**
     * shiro的拦截器是基于filter的，所以是在Filter中配置拦截规则
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        //设置自定义filter, roleAny对应RoleAnyFilter
        Map<String, Filter> filterMap = bean.getFilters();
        filterMap.put("roleAny", new RoleAnyFilter());

        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/index");
        bean.setUnauthorizedUrl("/unauthorized");

        // 由于使用FIRST MATCH WINS原则，必须使用LinkedHashMap
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        /**
         * https://waylau.gitbooks.io/apache-shiro-1-2-x-reference/content/III.%20Web%20Applications/10.%20Web.html
         * /index  使用 authc 拦截器
         * /login  使用 anon 拦截器，不拦截
         *
         * DefaultFilter中定义了拦截器
         * authc是使用FormAuthenticationFilter.class
         * anon是使用AnonymousFilter.class
         */
        filterChainDefinitionMap.put("/index", "authc");

        // AnonymousFilter.class接口不校验
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/loginUser", "anon");
        // druid请求不拦截，以便打开druid监控页面
        filterChainDefinitionMap.put("/druid/**", "anon");

        // admin接口只允许admin角色访问
        // RolesAuthorizationFilter.java 只覆盖了isAccessAllowed()方法
        filterChainDefinitionMap.put("/admin", "roles[admin]");


        /**
         * 一个星号*表示一级，两个星号**表示多级
         * 匹配到第一个规则后，链路就会短路，不再匹配后面的规则
         * 下面配置的结果是/test/add、/test/role/add都需要admin角色才能访问
         */
        //filterChainDefinitionMap.put("/**/add", "roles[admin]");
        filterChainDefinitionMap.put("/test/add", "user");
        filterChainDefinitionMap.put("/test/role/add", "anon");

        // /edit接口只有edit权限才能访问
        filterChainDefinitionMap.put("/edit", "perms[edit]");

        // url使用自定义自定义Filter
        //filterChainDefinitionMap.put("/any/role/**", "roleAny[admin]");
        filterChainDefinitionMap.put("/any/role/**", "roleAny[admin,customer]");

        // 所有接口使用 UserFilter.class接口，此接口校验规则是登陆后就返回true
        filterChainDefinitionMap.put("/**", "user");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return bean;
    }


    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);

        //manager.setSessionManager(customSessionManage);
        //manager.setCacheManager(redisCacheManager);

        return manager;
    }

    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher matcher) {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        // 缓存设置在Realm中更合适，设置在manager中的缓存最终也是给Realm使用
        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        return authRealm;
    }

    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher() {
        return new CredentialMatcher();
    }



    //@Bean
    //public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
    //    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    //    advisor.setSecurityManager(securityManager);
    //    return advisor;
    //}

    //@Bean
    //public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    //    DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
    //    creator.setProxyTargetClass(true);
    //    return creator;
    //}

}
