package com.hiteam.common.util.express;

import java.io.Serializable;


public class QueryParam implements Serializable{
    /**
     * 快递公司编号，详见http://code.google.com/p/kuaidi-api/wiki/Open_API_API_URL 或 http://www.kuaidi100.com/openapi/api_post.shtml
     */
    private String exComCode;
    /***
     * 快递编号
     */
    private String exBillCode;
    /***
     * 快递接口令牌
     */
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExComCode() {
        return exComCode;
    }

    public void setExComCode(String exComCode) {
        this.exComCode = exComCode;
    }

    public String getExBillCode() {
        return exBillCode;
    }

    public void setExBillCode(String exBillCode) {
        this.exBillCode = exBillCode;
    }
}
