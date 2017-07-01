package com.borsam.service.org.impl;

import com.borsam.repository.dao.org.OrganizationWalletDao;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.borsam.service.org.OrganizationWalletService;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service - 机构钱包
 * Created by Sebarswee on 2015/8/10.
 */
@Service("organizationWalletServiceImpl")
public class OrganizationWalletServiceImpl extends BaseServiceImpl<OrganizationWallet, Long> implements OrganizationWalletService {

    @Resource(name = "organizationWalletDaoImpl")
    public void setBaseDao(OrganizationWalletDao organizationWalletDao) {
        super.setBaseDao(organizationWalletDao);
    }
}
