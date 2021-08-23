package com.gzc.eshop.inventory.request;

import com.gzc.eshop.inventory.model.ProductInventory;
import com.gzc.eshop.inventory.service.ProductInventoryService;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductInventoryDBUpdateRequest
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/30 22:38
 * Description:
 */

public class ProductInventoryDBUpdateRequest implements Request {

    /**
     * 商品库存
     */
    private ProductInventory productInventory;
    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;


    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        System.out.println("===========日志===========: 数据库更新请求开始执行，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
        // 删除redis中的缓存
        productInventoryService.removeProductInventoryCache(productInventory);

        // 为了模拟演示先删除了redis中的缓存，然后还没更新数据库的时候，读请求过来了，这里可以人工sleep一下
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        // 修改数据库中的库存
        productInventoryService.updateProductInventory(productInventory);

    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
