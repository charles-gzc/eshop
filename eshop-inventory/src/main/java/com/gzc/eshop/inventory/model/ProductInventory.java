package com.gzc.eshop.inventory.model;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductInventory
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/30 22:40
 * Description:
 */

/**
 * 库存数量model
 * @author Administrator
 *
 */
public class ProductInventory {

    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 库存数量
     */
    private Long inventoryCnt;

    public ProductInventory() {

    }


    public ProductInventory(Integer productId, Long inventoryCnt) {
        this.productId = productId;
        this.inventoryCnt = inventoryCnt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getInventoryCnt() {
        return inventoryCnt;
    }

    public void setInventoryCnt(Long inventoryCnt) {
        this.inventoryCnt = inventoryCnt;
    }
}
