package com.gzc.eshop.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gzc.eshop.inventory.dao.RedisDAO;
import com.gzc.eshop.inventory.mapper.UserMapper;
import com.gzc.eshop.inventory.model.User;
import com.gzc.eshop.inventory.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: UserServiceImpl
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/19 22:13
 * Description:
 */

@Service
public class UserServiceImpl  implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisDAO redisDAO;

    @Override
    public User findUserInfo() {
        return userMapper.findUserInfo();
    }

    @Override
    public User getCachedUserInfo() {
        redisDAO.set("cached_user","{\"name\": \"zhangsan\", \"age\": 25}");
        String json = redisDAO.get("cached_user");

        JSONObject jsonObject = JSONObject.parseObject(json);
        User user = new User();
        user.setName(jsonObject.getString("name"));
        user.setAge(jsonObject.getInteger("age"));
        return user;
    }
}
