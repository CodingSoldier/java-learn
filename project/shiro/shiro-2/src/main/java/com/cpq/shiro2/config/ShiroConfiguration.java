package com.cpq.shiro2.config;

import com.cpq.shiro2.filter.RoleAnyFilter;
import com.cpq.shiro2.session.CustomSessionManage;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
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
        bean.setUnauthorizedUrl("/unauthorized");

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        /**
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

        // /admin接口只允许admin角色访问
        filterChainDefinitionMap.put("/admin", "roles[admin]");

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

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        manager.setSessionManager(customSessionManage);
        //manager.setCacheManager(redisCacheManager);
        manager.setCacheManager(new MemoryConstrainedCacheManager());
        return manager;
    }

    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher matcher) {
        AuthRealm authRealm = new AuthRealm();
        //authRealm.setCacheManager(redisCacheManager);
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher() {
        return new CredentialMatcher();
    }



    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}
