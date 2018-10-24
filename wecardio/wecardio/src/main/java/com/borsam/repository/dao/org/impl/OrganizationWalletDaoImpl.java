package com.borsam.repository.dao.org.impl;

import com.borsam.repository.dao.org.OrganizationWalletDao;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.LockModeType;
import java.math.BigDecimal;

/**
 * Dao - 机构钱包
 * Created by tantian on 2015/8/10.
 */
@Repository("organizationWalletDaoImpl")
public class OrganizationWalletDaoImpl extends BaseDaoImpl<OrganizationWallet, Long> implements OrganizationWalletDao {
    @Override
    public void adjustTotalInSellService(Organization org, BigDecimal servicePrice) {
        Assert.notNull(servicePrice);

        //钱包信息
        OrganizationWallet organizationWallet = this.find(org.getId(), LockModeType.PESSIMISTIC_WRITE);
        //根据分成计算(分成收入=服务包价格*分成比例)，分成比例保存为小数点，非80,90
        BigDecimal realTotal = servicePrice.multiply(org.getRate());
        //销售总额
        organizationWallet.setTotal(organizationWallet.getTotal().add(servicePrice));
        //分成总金额
        organizationWallet.setRealTotal(organizationWallet.getRealTotal().add(realTotal));
        // 累计分成金额
        organizationWallet.setGrandTotal(organizationWallet.getGrandTotal().add(realTotal));

        this.merge(organizationWallet);
    }
}
