package com.gzc.eshop.inventory.service;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductInventoryService
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/30 23:16
 * Description:
 */

import com.gzc.eshop.inventory.model.ProductInventory;

/**
 * 商品库存Service接口
 * @author Administrator
 *
 */
public interface ProductInventoryService {

    /**
     * 更新商品库存
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除Redis中的商品库存的缓存
     * @param productInventory 商品库存
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return 商品库存
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    void setProductInventoryCache(ProductInventory productInventory);


    /**
     * 获取商品库存的缓存
     * @param productId
     * @return
     */
    ProductInventory getProductInventoryCache(Integer productId);
}
