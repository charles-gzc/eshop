package com.gzc.eshop.inventory.service.impl;

import com.gzc.eshop.inventory.dao.RedisDAO;
import com.gzc.eshop.inventory.mapper.ProductInventoryMapper;
import com.gzc.eshop.inventory.model.ProductInventory;
import com.gzc.eshop.inventory.service.ProductInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductInventoryServiceImpl
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/30 23:23
 * Description:
 */

@Service("productInventoryService")
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Resource
    ProductInventoryMapper productInventoryMapper;
    @Resource
    RedisDAO redisDAO;

    /**
     * 更新商品库存
     * @param productInventory 商品库存
     */
    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
        System.out.println("===========日志===========: 已修改数据库中的库存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
    }

    /**
     * 删除Redis中的商品库存的缓存
     * @param productInventory 商品库存
     */
    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.delete(key);
        System.out.println("===========日志===========: 已删除redis中的缓存，key=" + key);
    }

    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return 商品库存
     */
    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.set(key,String.valueOf(productInventory.getInventoryCnt()));
        System.out.println("===========日志===========: 已更新商品库存的缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt() + ", key=" + key);
    }

    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt = 0L;

        String key = "product:inventory:" + productId;
        String result = redisDAO.get(key);
        if (result != null && !"".equals(result)){
            try {
                inventoryCnt = Long.valueOf(result);
                return new ProductInventory(productId, inventoryCnt);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
