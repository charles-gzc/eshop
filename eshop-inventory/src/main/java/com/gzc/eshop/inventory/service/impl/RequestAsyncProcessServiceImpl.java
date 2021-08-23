package com.gzc.eshop.inventory.service.impl;

import com.gzc.eshop.inventory.request.Request;
import com.gzc.eshop.inventory.request.RequestQueue;
import com.gzc.eshop.inventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RequestAsyncProcessServiceImpl
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/2 17:32
 * Description:请求异步处理的service实现
 */

@Service("requestAsyncProcessService")
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {


    @Override
    public void process(Request request) {


        try {
            // 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
            // 将请求放入对应的队列中，完成路由操作
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayBlockingQueue<Request> getRoutingQueue(Integer productId){
        RequestQueue requestQueue = RequestQueue.getInstance();

        // 先获取productId的hash值
        String key = String.valueOf(productId);
        int h;
        int hash = key == null ? 0 :(h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.queueSize() - 1)&hash;

        System.out.println("===========日志===========: 路由内存队列，商品id=" + productId + ", 队列索引=" + index);
        return requestQueue.getQueue(index);

    }
}
