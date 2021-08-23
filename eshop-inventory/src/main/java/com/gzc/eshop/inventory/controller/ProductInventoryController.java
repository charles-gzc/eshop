package com.gzc.eshop.inventory.controller;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: ProductInventoryController
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/2 17:44
 * Description:商品库存Controller
 */

import com.gzc.eshop.inventory.model.ProductInventory;
import com.gzc.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.gzc.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.gzc.eshop.inventory.service.ProductInventoryService;
import com.gzc.eshop.inventory.service.RequestAsyncProcessService;
import com.gzc.eshop.inventory.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class ProductInventoryController {

    @Resource
    private ProductInventoryService productInventoryService;
    @Resource
    private RequestAsyncProcessService requestAsyncProcessService;

    /**
     * 更新商品库存
     */
    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public Response updateProductInventory(ProductInventory productInventory){
        System.out.println("===========日志===========: 接收到更新商品库存的请求，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
        Response response = null;


        ProductInventoryDBUpdateRequest updateRequest = null;
        try {
            updateRequest = new ProductInventoryDBUpdateRequest(productInventory, productInventoryService);
            requestAsyncProcessService.process(updateRequest);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            response = new Response(Response.FAILURE);
        }

        return response;
    }

    /**
     * 获取商品库存
     */
    @RequestMapping("/getProductInventory")
    @ResponseBody
    public ProductInventory getProductInventory(Integer productId){
        System.out.println("===========日志===========: 接收到一个商品库存的读请求，商品id=" + productId);
        ProductInventory productInventory = null;

        try {
            ProductInventoryCacheRefreshRequest refreshRequest = new ProductInventoryCacheRefreshRequest(
                    productId, productInventoryService,false);
            requestAsyncProcessService.process(refreshRequest);

            // 将请求扔给service异步去处理以后，就需要while(true)一会儿，在这里hang住
            // 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            while(true){
                if (waitTime > 10000){
                    break;
                }
                // 尝试去redis中读取一次商品库存的缓存数据
                productInventory = productInventoryService.getProductInventoryCache(productId);

                // 如果读取到了结果，那么就返回
                if (productInventory != null){
                    System.out.println("===========日志===========: 在200ms内读取到了redis中的库存缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
                    return productInventory;
                }

                // 如果没有读取到结果，那么等待一段时间
                else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }
            // 直接尝试从数据库中读取数据
            productInventory = productInventoryService.findProductInventory(productId);
            if (productInventory != null){
                // 将缓存刷新一下
                // 这个过程，实际上是一个读操作的过程，要放在队列中串行去处理，避免数据不一致的问题
                refreshRequest = new ProductInventoryCacheRefreshRequest(
                        productId, productInventoryService,true);
                requestAsyncProcessService.process(refreshRequest);

                // 代码会运行到这里，只有三种情况：
                // 1、就是说，上一次也是读请求，数据刷入了redis，但是redis LRU算法给清理掉了，标志位还是false
                // 所以此时下一个读请求是从缓存中拿不到数据的，再放一个读Request进队列，让数据去刷新一下
                // 2、可能在200ms内，就是读请求在队列中一直积压着，没有等待到它执行（在实际生产环境中，基本是比较坑了）
                // 所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求
                // 3、数据库里本身就没有，缓存穿透，穿透redis，请求到达mysql库
                return productInventory;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductInventory(productId,-1L);
    }
}
