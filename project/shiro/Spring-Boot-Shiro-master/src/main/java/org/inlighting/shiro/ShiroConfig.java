package org.inlighting.shiro;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(MyRealm realm) {
        realm.setCacheManager(new MemoryConstrainedCacheManager());
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        // 缓存设置在Realm中更合适，设置在manager中的缓存最终也是给Realm使用，无状态下没用
        /**
         * 默认未赋值，false
         * org.apache.shiro.realm.AuthenticatingRealm#getAvailableAuthenticationCache()
         * org.apache.shiro.realm.AuthenticatingRealm#getCachedAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
         *
         * 在 AuthenticatingRealm 构造函数中，将 AuthenticationCachingEnabled 设置为了false
         * 但是在 AuthorizingRealm 构造函数中，将 AuthorizationCachingEnabled 设置为了true
         * 这就是为什么使用token方式认证的时候，总会执行 doGetAuthenticationInfo
         * 但是不会执行 doGetAuthorizationInfo
         *
         * AuthenticationCache、AuthorizationCache都存储在CacheManager中，key分别是
         * this.authenticationCacheName = this.getClass().getName() + ".authenticationCache";
         * this.authorizationCacheName = this.getClass().getName() + ".authorizationCache";
         *
         * 得到一个key-value的缓存（Map），而key就是principals
         *
         */
        //realm.setAuthenticationCachingEnabled(true);
        //realm.setAuthorizationCachingEnabled(true);

        // 默认就是true
        //realm.setCachingEnabled(true);

        /**
         * 不能使用这个MemoryConstrainedCacheManager，不然
         * AuthenticatingRealm.getAuthenticationInfo()方法中获取缓存永远为空
         */
        realm.setCacheManager(new MemoryConstrainedCacheManager());
        manager.setRealm(realm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         *
         * 这将防止 Shiro 使用 Subject 的会话来存储所有跨请求/调用/消息的Subject 状态。只要确保你对每个请求进行了身份验证，这样 Shiro 将会对给定的请求/调用/消息知道它的 Subject 是谁。
         */
        //DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        //DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        //defaultSessionStorageEvaluator.setSessionStorageEnabled(true);
        //subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        //manager.setSubjectDAO(subjectDAO);

        // web应用默认使用基于容器的SessionManager
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        // 一小时
        Long timeout = 1000 * 60 * 60L;
        sessionManager.setGlobalSessionTimeout(timeout);
        manager.setSessionManager(sessionManager);

        return manager;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);

        factoryBean.setSecurityManager(securityManager);
        factoryBean.setUnauthorizedUrl("/401");

        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new HashMap<>();
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/login", "anon");
        filterRuleMap.put("/**", "jwt");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
