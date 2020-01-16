package com.example.shirojwt;


import com.example.shirojwt.filter.CustomPermissionsAuthorizationFilter;
import com.example.shirojwt.filter.JwtFilter;
import com.example.shirojwt.model.UserService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;
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

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("jwtRealm") JwtRealm jwtRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 设置realm
        manager.setRealm(jwtRealm);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        //添加filter，factoryBean.getFilters()获取到的是引用，可直接添加值
        factoryBean.getFilters().put("jwt", new JwtFilter());
        factoryBean.getFilters().put("customPerms", new CustomPermissionsAuthorizationFilter());

        Map<String, String> origin = factoryBean.getFilterChainDefinitionMap();
        LinkedHashMap<String, String> definitionMap = new LinkedHashMap();
        definitionMap.putAll(origin);

        /**
         * put的顺序很重要，当/open/**匹配到请求url后，后面的匹配规则/**不起作用
         * 官方将这种原则称为 FIRST MATCH WINS
         * https://waylau.gitbooks.io/apache-shiro-1-2-x-reference/content/III.%20Web%20Applications/10.%20Web.html
         */
        definitionMap.put("/open/**", "anon");

        // 似乎只能使用注解校验权限
        definitionMap.put("/user/delete", "jwt, customPerms[delete]");
        definitionMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(definitionMap);
        return factoryBean;
    }



}
