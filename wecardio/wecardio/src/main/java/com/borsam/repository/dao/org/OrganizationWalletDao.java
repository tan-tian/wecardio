package com.borsam.repository.dao.org;

import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.hiteam.common.base.repository.dao.BaseDao;

import java.math.BigDecimal;

/**
 * Dao - 机构钱包
 * Created by tantian on 2015/8/10.
 */
public interface OrganizationWalletDao extends BaseDao<OrganizationWallet, Long> {
    /**
     * 购买服务时，调整收入（总收入及分成后的总收入)
     * @param organization 机构
     * @param servicePrice 服务包价格
     */
    public void adjustTotalInSellService(Organization organization, BigDecimal servicePrice);
}
