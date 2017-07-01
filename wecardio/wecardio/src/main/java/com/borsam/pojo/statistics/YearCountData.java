package com.borsam.pojo.statistics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hiteam.common.base.repository.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 * @Description: 年销售统计
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-12 16:10
 * </pre>
 */
@JsonAutoDetect
public class YearCountData implements Serializable {

    /**累计销售总金额*/
    private BigDecimal grossSalesTotal = new BigDecimal(0);

    /**销售金额：只限于一年的销售金额*/
    private BigDecimal salesTotal = new BigDecimal(0);
    /***
     * 平台对应实体：PlatformSettlement
     * 机构对应实体：OrgSettlement
     */
    public List<? extends BaseEntity> datas;

    public List<? extends BaseEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<? extends BaseEntity> datas) {
        this.datas = datas;
    }

    public BigDecimal getGrossSalesTotal() {
        return grossSalesTotal;
    }

    public void setGrossSalesTotal(BigDecimal grossSalesTotal) {
        this.grossSalesTotal = grossSalesTotal;
    }

    public BigDecimal getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(BigDecimal salesTotal) {
        this.salesTotal = salesTotal;
    }
}
