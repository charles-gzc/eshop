package com.gzc.eshop.cache.service;

import com.gzc.eshop.cache.model.ProductInfo;
import com.gzc.eshop.cache.model.ShopInfo;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: CacheService
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/6 15:38
 * Description:
 */

public interface CacheService {

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    public ProductInfo getLocalCahce(Long id);

    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    public ProductInfo setLocalCache(ProductInfo productInfo);

    /**
     * 将商品信息保存到本地的ehcache缓存中
     * @param productInfo
     */
    public ProductInfo saveProductInfoToLocalCache(ProductInfo productInfo);

    /**
     * 从本地ehcache缓存中获取商品信息
     * @param productId
     * @return
     */
    public ProductInfo getProductInfoFromLocalCache(Long productId);

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     * @param shopInfo
     */
    public ShopInfo saveShopInfoToLocalCache(ShopInfo shopInfo);

    /**
     * 从本地ehcache缓存中获取店铺信息
     * @param shopId
     * @return
     */
    public ShopInfo getShopInfoFromLocalCache(Long shopId);

    /**
     * 将商品信息保存到redis中
     * @param productInfo
     */
    public void saveProductInfoToRedisCache(ProductInfo productInfo);

    /**
     * 将店铺信息保存到redis中
     * @param shopInfo
     */
    public void saveShopInfoToRedisCache(ShopInfo shopInfo);

    /**
     * 从redis中获取商品信息
     */
    public ProductInfo getProductInfoFromReidsCache(Long productId);

    /**
     * 从redis中获取店铺信息
     */
    public ShopInfo getShopInfoFromReidsCache(Long shopId);
}
