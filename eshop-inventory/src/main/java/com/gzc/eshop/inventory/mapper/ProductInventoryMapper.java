package com.gzc.eshop.inventory.mapper;

import com.gzc.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductInventoryMapper
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/11/30 23:19
 * Description:
 */

/**
 * 库存数量Mapper
 * @author Administrator
 *
 */
public interface ProductInventoryMapper {

    /**
     * 更新库存数量
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存信息
     * @param productId 商品id
     * @return 商品库存信息
     */
    ProductInventory findProductInventory(@Param("productId") Integer productId);
}
