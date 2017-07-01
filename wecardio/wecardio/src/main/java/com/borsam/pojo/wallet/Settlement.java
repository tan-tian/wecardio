package com.borsam.pojo.wallet;

import java.math.BigDecimal;

/**
 * Bean - 机构月结信息
 * Created by Sebarswee on 2015/8/11.
 */
public class Settlement {

    private int year;                   // 年份
    private int month;                  // 月份
    private BigDecimal money;           // 金额
    private Boolean selected = false;   // 是否选择

    public Settlement(int year, int month, BigDecimal money) {
        this.year = year;
        this.month = month;
        this.money = money;
    }

    public Settlement(int year, int month, BigDecimal money, Boolean selected) {
        this.year = year;
        this.month = month;
        this.money = money;
        this.selected = selected;
    }

    /**
     * 获取年份
     * @return 年份
     */
    public int getYear() {
        return year;
    }

    /**
     * 设置年份
     * @param year 年份
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * 获取月份
     * @return 月份
     */
    public int getMonth() {
        return month;
    }

    /**
     * 设置月份
     * @param month 月份
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * 获取金额
     * @return 金额
     */
    public BigDecimal getMoney() {
        return money.setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 设置金额
     * @param money 金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取是否选择
     * @return 是否选择
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * 设置是否选择
     * @param selected 是否选择
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
