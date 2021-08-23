package com.gzc.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gzc.eshop.cache.model.ProductInfo;
import com.gzc.eshop.cache.model.ShopInfo;
import com.gzc.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: CacheServiceImpl
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/6 15:40
 * Description:缓存Service实现类
 */

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Resource
    private JedisCluster jedisCluster;

    public static final String CACHE_NAME = "local";

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    @Cacheable(value = CACHE_NAME,key = "'key_'+#id")
    @Override
    public ProductInfo getLocalCahce(Long id) {
        return null;
    }

    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    @CachePut(value = CACHE_NAME,key = "'key_'+#productInfo.getId()")
    @Override
    public ProductInfo setLocalCache(ProductInfo productInfo) {
        return productInfo;
    }


    /**
     * 将商品信息保存到本地的ehcache缓存中
     * @param productInfo
     */
    @CachePut(value = CACHE_NAME,key = "'product_info_'+#productInfo.getId()")
    @Override
    public ProductInfo saveProductInfoToLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地ehcache缓存中获取商品信息
     * @param productId
     * @return
     */
    @Cacheable(value = CACHE_NAME,key = "'product_info_'+#productId")
    @Override
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     * @param shopInfo
     */
    @CachePut(value = CACHE_NAME,key = "'shop_info_'+#shopInfo.getId()")
    @Override
    public ShopInfo saveShopInfoToLocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    /**
     * 从本地ehcache缓存中获取店铺信息
     * @param shopId
     * @return
     */
    @Cacheable(value = CACHE_NAME,key = "'shop_info_'+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 将商品信息保存到redis中
     * @param productInfo
     */
    @Override
    public void saveProductInfoToRedisCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));
    }

    /**
     * 将店铺信息保存到redis中
     * @param shopInfo
     */
    @Override
    public void saveShopInfoToRedisCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));
    }

    /**
     * 从redis中获取商品信息
     * @param productId
     */
    @Override
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
        String key = "product_info_" + productId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json,ProductInfo.class);
    }

    /**
     * 从redis中获取店铺信息
     * @param shopId
     */
    @Override
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        String key = "shop_info_" + shopId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json,ShopInfo.class);
    }
}
