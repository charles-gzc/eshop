package com.gzc.eshop.inventory;

import com.gzc.eshop.inventory.listener.InitListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.sql.DataSource;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: Application
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/19 20:49
 * Description: 库存Application入口类
 */


@SpringBootApplication
@MapperScan("com.gzc.eshop.inventory.mapper")
public class Application {

    /**
     * 构建数据源
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }

    /**
     * 构建Mybatis的入口类 SqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 构建事务管理器
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JedisCluster JedisClusterFactory() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.31.174", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.31.100", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.31.100", 7004));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);
        return jedisCluster;
    }

    /**
     * 注册监听器
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new InitListener());
        return  registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
