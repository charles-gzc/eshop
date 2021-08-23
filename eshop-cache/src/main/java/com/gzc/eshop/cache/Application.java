package com.gzc.eshop.cache;

import com.gzc.eshop.cache.listener.InitListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@MapperScan("com.gzc.eshop.cache.mapper")
public class Application {
   	/*@Bean
   	@ConfigurationProperties(prefix="spring.datasource")
   	public DataSource dataSource() {
   	    return new org.apache.tomcat.jdbc.pool.DataSource();
   	}
   	
   	@Bean
   	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
   	    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
   	    sqlSessionFactoryBean.setDataSource(dataSource());
   	    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
   	    sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
   	    return sqlSessionFactoryBean.getObject();
   	}
   	 
   	@Bean
   	public PlatformTransactionManager transactionManager() {
   	    return new DataSourceTransactionManager(dataSource());
   	}*/

   	@Bean
   	public ServletListenerRegistrationBean servletListenerRegistrationBean(){
		ServletListenerRegistrationBean servletListenerRegistrationBean =
				new ServletListenerRegistrationBean();
		servletListenerRegistrationBean.setListener(new InitListener());
		return servletListenerRegistrationBean;
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
   	
   	public static void main(String[] args) {
   	    SpringApplication.run(Application.class, args);
   	}
}