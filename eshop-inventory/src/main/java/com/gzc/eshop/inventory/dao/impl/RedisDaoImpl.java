package com.gzc.eshop.inventory.dao.impl;

import com.gzc.eshop.inventory.dao.RedisDAO;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RedisDaoImpl
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/19 22:56
 * Description:
 */

@Repository("redisDAO")
public class RedisDaoImpl implements RedisDAO {

    @Resource
    private JedisCluster jedisCluster;
    @Override
    public void set(String key, String value) {
        jedisCluster.set(key,value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public void delete(String key) {
         jedisCluster.del(key);
    }
}
