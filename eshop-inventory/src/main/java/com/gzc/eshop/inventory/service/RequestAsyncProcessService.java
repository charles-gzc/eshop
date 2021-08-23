package com.gzc.eshop.inventory.service;

import com.gzc.eshop.inventory.request.Request;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: RequestAsyncProcessService
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/2 17:30
 * Description:请求异步执行的service
 */

public interface RequestAsyncProcessService {

    void process(Request request);
}
