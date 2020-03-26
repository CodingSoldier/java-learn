package com.example.multidatasource.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @Description TODO
 * @Author bingfeng
 * @Date 2019/11/20 17:09
 */
@Configuration
@MapperScan(basePackages = "com.poly.unifiedportalsyndata.tdportalnew.**.mapper",
        sqlSessionFactoryRef = ShiroDataSourcesConfig.SQL_SESSION_FACTORY)
public class ShiroDataSourcesConfig {

    public static final String DATABASE_PREFIX = "spring.datasource.shiro.";
    public static final String DATA_SOURCE_NAME = "shiroDataSource";
    public static final String SQL_SESSION_FACTORY = "shiroSqlSessionFactory";

    @Primary
    @Bean(DATA_SOURCE_NAME)
    public DataSource dataSource(Environment environment) {
        return DataSourceUtil.createAtomikosDataSourceBean(DATA_SOURCE_NAME, environment, DATABASE_PREFIX);
    }

    @Primary
    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DATA_SOURCE_NAME) DataSource dataSource) throws Exception {
        return DataSourceUtil.createSqlSessionFactory(dataSource);
    }

}
