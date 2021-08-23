package com.gzc.eshop.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RequestQueue
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/26 21:46
 * Description: 请求内存队列
 */

public class RequestQueue {

    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();
    /**
     * 标识位map
     */
    private Map<Integer,Boolean> flagMap = new ConcurrentHashMap<Integer,Boolean>();

    private static class Singleton{
        private static RequestQueue instance;

        static{
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance(){
            return instance;
        }
    }

    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 添加一个内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue){
        this.queues.add(queue);
    }

    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize(){
        return queues.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index){
        return queues.get(index);
    }

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }
}
