package com.gzc.eshop.inventory.listener;

import com.gzc.eshop.inventory.thread.RequestProcessorThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: InitListener
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/25 21:30
 * Description:
 */

public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 初始化工作线程池和内存队列
        RequestProcessorThreadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
