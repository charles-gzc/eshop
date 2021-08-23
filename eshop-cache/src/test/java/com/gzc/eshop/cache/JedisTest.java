package com.gzc.eshop.cache;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {
	
	public static void main(String[] args) throws Exception {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.31.174", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.31.100", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.31.217", 7006));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);
        System.out.println(jedisCluster.get("product_info_1"));   
        System.out.println(jedisCluster.get("shop_info_1"));  
	}

}
