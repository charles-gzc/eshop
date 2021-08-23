package com.gzc.eshop.inventory.controller;

import com.gzc.eshop.inventory.model.User;
import com.gzc.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: UserController
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/19 22:21
 * Description:
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo(){
        User user = userService.findUserInfo();
        return user;
    }

    @RequestMapping("/getCacheUserInfo")
    @ResponseBody
    public User getCacheUserInfo(){
        User user = userService.getCachedUserInfo();
        return user;
    }
}
