package com.gzc.eshop.inventory.request;

/**
 * @author gaozhenchao
 * @version V1.0
 * @Title: TODO
 * @Description: 请求接口
 * @date 2020/11/26 21:44
 **/

public interface Request {
    void process();
    Integer getProductId();
    boolean isForceRefresh();
}
