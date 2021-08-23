package com.gzc.eshop.cache.listener;

import com.gzc.eshop.cache.kafka.KafkaConcusmer;
import com.gzc.eshop.cache.rebuild.RebuildCacheThread;
import com.gzc.eshop.cache.spring.SpringContext;
import com.gzc.eshop.cache.zk.ZookeeperSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: InitListener
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/6 22:10
 * Description:
 */

public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //获取spring容器
        ServletContext sc = sce.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);
        new Thread(new KafkaConcusmer("cache-message")).start();
        new Thread(new RebuildCacheThread()).start();
        ZookeeperSession.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
