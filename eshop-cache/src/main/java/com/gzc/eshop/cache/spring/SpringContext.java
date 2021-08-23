package com.gzc.eshop.cache.spring;


import org.springframework.context.ApplicationContext;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: SpringContext
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/6 23:45
 * Description:
 */

public class SpringContext {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext){
        SpringContext.applicationContext = applicationContext;
    }

}
