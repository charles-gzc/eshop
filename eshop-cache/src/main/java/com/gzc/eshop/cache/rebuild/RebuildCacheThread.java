package com.gzc.eshop.cache.rebuild;

import com.gzc.eshop.cache.model.ProductInfo;
import com.gzc.eshop.cache.service.CacheService;
import com.gzc.eshop.cache.spring.SpringContext;
import com.gzc.eshop.cache.zk.ZookeeperSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RebuildCacheThread
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/17 7:34
 * Description: 缓存重建线程
 */

public class RebuildCacheThread implements Runnable {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
        ZookeeperSession zookeeperSession = ZookeeperSession.getInstance();
        CacheService cacheService = (CacheService)SpringContext.getApplicationContext().getBean("cacheService");

        while (true){
            ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();

            zookeeperSession.acquireDistributedLock(productInfo.getId());
            ProductInfo existedProductInfo = cacheService.getProductInfoFromReidsCache(productInfo.getId());

            if (existedProductInfo !=null){
                // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
                try {
                    Date date = sdf.parse(productInfo.getModifiedTime());
                    Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());

                    if (date.before(existedDate)){
                        System.out.println("current date[" + productInfo.getModifiedTime() + "] is before existed date[" + existedProductInfo.getModifiedTime() + "]");
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("current date[" + productInfo.getModifiedTime() + "] is after existed date[" + existedProductInfo.getModifiedTime() + "]");
            }else {
                System.out.println("existed product info is null......");
            }

            cacheService.saveProductInfoToLocalCache(productInfo);
            cacheService.saveProductInfoToRedisCache(productInfo);

            //释放锁
            zookeeperSession.releaseDistributedLock(productInfo.getId());
        }
    }
}
