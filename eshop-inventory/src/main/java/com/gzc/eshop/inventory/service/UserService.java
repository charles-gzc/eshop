package com.gzc.eshop.inventory.service;

import com.gzc.eshop.inventory.model.User;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: UserService
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/19 22:02
 * Description:
 */

public interface  UserService {

    /**
     * 查询用户信息
     * @return
     */
    public User findUserInfo();

    /**
     * 查询redis中缓存的用户信息
     * @return
     */
    public User getCachedUserInfo();
}
