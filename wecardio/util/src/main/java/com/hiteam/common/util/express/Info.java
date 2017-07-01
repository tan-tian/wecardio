package com.hiteam.common.util.express;

import java.io.Serializable;
import java.util.Date;


public class Info implements Serializable{
    /**快递时间*/
    private Date time;
    /**快递内容*/
    private String content;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
