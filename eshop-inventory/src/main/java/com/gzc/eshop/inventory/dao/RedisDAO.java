package com.gzc.eshop.inventory.dao;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RedisDAO
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/19 22:55
 * Description:
 */

public interface RedisDAO {

    void set(String key, String value);
    String get(String key);
    void delete(String key);
}
