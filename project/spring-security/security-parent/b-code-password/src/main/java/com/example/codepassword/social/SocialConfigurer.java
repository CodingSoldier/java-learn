package com.example.codepassword.social;

import com.example.codepassword.social.qq.CustomSpringSocialConfigurer;
import com.example.codepassword.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;


@EnableSocial
@Configuration
//@ConditionalOnProperty(prefix = "social.qq", name = "app-id") 暂无效
public class SocialConfigurer extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CustomConnectionSignUp customConnectionSignUp;

    @Value("${social.qq.provider-id}")
    String providerId;
    @Value("${social.qq.app-id}")
    String appId;
    @Value("${social.qq.app-secret}")
    String appSecret;
    @Value("${social.qq.filterProcessesUrl}")
    String filterProcessesUrl;
    @Value("${social.qq.sign-up}")
    String signUp;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
                                       Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    public ConnectionFactory createConnectionFactory(){
        return new QQConnectionFactory(providerId, appId, appSecret);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    /**
     * 需要在数据库建表  UserConnection
     * 建表SQL在JdbcUsersConnectionRepository.java同一个包中
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        //自动注册，不跳转注册页面，中文会乱码
        repository.setConnectionSignUp(customConnectionSignUp);
        return repository;
    }

    // 社交配置类，需要apply到CustomWebSecurityConfiguration中
    @Bean
    public SpringSocialConfigurer springSocialConfigurer(){
        CustomSpringSocialConfigurer ssc = new CustomSpringSocialConfigurer(filterProcessesUrl);
        ssc.signupUrl(signUp);
        return ssc;
    }

    // 通过此工具类拿社交信息
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator));
    }


}
