package com.borsam.pojo.statistics;

import java.io.Serializable;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-16 19:50
 * </pre>
 */
public class SettlementSumData implements Serializable{
    /**销售年份*/
    private Integer year;
    /**销售月份*/
    private Integer month;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public SettlementSumData() {
    }

    public SettlementSumData(Integer year, Integer month) {
        this.year = year;
        this.month = month;
    }
}
