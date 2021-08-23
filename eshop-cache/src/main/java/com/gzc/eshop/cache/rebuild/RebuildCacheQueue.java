package com.gzc.eshop.cache.rebuild;

import com.gzc.eshop.cache.model.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RebuildCacheQueue
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/17 7:27
 * Description:
 */

public class RebuildCacheQueue {

   private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<ProductInfo>(1000);

   public void putProductInfo(ProductInfo productInfo){
       try {
           queue.put(productInfo);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

    public ProductInfo takeProductInfo(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 内部单例类
     * @author Administrator
     *
     */
    private static class Singleton{
        private static RebuildCacheQueue instance;
        static{
            instance = new RebuildCacheQueue();
        }
        private static RebuildCacheQueue getInstance(){
            return instance;
        }
    }

    public static RebuildCacheQueue getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){
        getInstance();
    }
}
