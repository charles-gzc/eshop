package com.gzc.eshop.storm.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ZookeeperSession
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/16 21:15
 * Description:
 */

public class ZookeeperSession {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zookeeper;
    // 去连接zookeeper server，创建会话的时候，是异步去进行的
    // 所以要给一个监听器，说告诉我们什么时候才是真正完成了跟zk server的连接
    public ZookeeperSession(){
        try {
            this.zookeeper = new ZooKeeper(
                    "192.168.31.174:2181,192.168.31.100:2181,192.168.31.217:2181",
                    5000,
                    new ZookeeperWatch());
            // 给一个状态CONNECTING，连接中
            System.out.println(zookeeper.getState());

            try {
                // CountDownLatch
                // java多线程并发同步的一个工具类
                // 会传递进去一些数字，比如说1,2 ，3 都可以
                // 然后await()，如果数字不是0，那么久卡住，等待

                // 其他的线程可以调用coutnDown()，减1
                // 如果数字减到0，那么之前所有在await的线程，都会逃出阻塞的状态
                // 继续向下运行
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ZooKeeper session established......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     - 获取分布式锁
     - @param productId
     */
    public void acquireDistributedLock() {
        String path = "/taskid-list-lock" ;
        try {
            zookeeper.create(path,"".getBytes(),
                    Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success to acquire lock for taskid-list-lock");
        } catch (Exception e) {
            // 如果那个商品对应的锁的node，已经存在了，就是已经被别人加锁了，那么就这里就会报错
            // NodeExistsException
            int count = 0;
            while(true){
                //报错之后尝试加锁，失败后进入下一次循环加锁，直到加锁成功退出循环
                try {
                    Thread.sleep(20);
                    zookeeper.create(path,"".getBytes(),
                            Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e1) {
                    count++;
                    System.out.println("the " + count + " times try to acquire lock for taskid-list-lock......");
                   continue;
                }
                System.out.println("success to acquire lock for taskid-list-lock after " + count + " times try......");
                break;
            }
        }
    }

    /**
     - 释放掉一个分布式锁
     - @param productId
     */
    public void releaseDistributedLock() {
        String path = "/taskid-list-lock";
        try {
            zookeeper.delete(path,-1);
            System.out.println("release the lock for taskid-list-lock......");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void createNode(String path) {
        try {
            zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {

        }
    }

    public String getNodeData(){

        try {
            return new String(zookeeper.getData("/taskid-list",false,new Stat()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setNodeData(String path,String data){
        try {
            zookeeper.setData(path,data.getBytes(),-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ZookeeperWatch implements Watcher{

        @Override
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event: " + event.getState());
            if (KeeperState.SyncConnected == event.getState()){
                countDownLatch.countDown();
            }
        }
    }


    /**
     - 封装单例的静态内部类
     - @author Administrator
     *
     */
    private static class Singleton{
        private static ZookeeperSession zookeeperSession;

        static {
            zookeeperSession = new ZookeeperSession();
        }

        private static ZookeeperSession getInstance(){
            return zookeeperSession;
        }
    }

    /**
     - 获取单例
     - @return
     */
    public static ZookeeperSession getInstance(){
        return Singleton.getInstance();
    }

    /**
     - 初始化单例的便捷方法
     */
    public static void init(){
        getInstance();
    }
}
