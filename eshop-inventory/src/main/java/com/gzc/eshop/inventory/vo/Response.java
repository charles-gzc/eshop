package com.gzc.eshop.inventory.vo;

/**
 * Copyright (C), 2015-2020, XXX有限公司
 * ClassName: Response
 * Author:   gzc
 * E-mail:   1226046769@qq.com
 * Date:     2020/12/2 17:46
 * Description:
 */

public class Response {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    private String status;
    private String message;

    public Response() {

    }

    public Response(String status) {
        this.status = status;
    }

    public Response(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
