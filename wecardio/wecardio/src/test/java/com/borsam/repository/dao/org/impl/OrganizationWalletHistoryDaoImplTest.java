package com.borsam.repository.dao.org.impl;

import com.borsam.pojo.statistics.SettlementOrgSumData;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-15 21:14
 * </pre>
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class OrganizationWalletHistoryDaoImplTest {

    @Resource(name = "organizationWalletHistoryDaoImpl")
    private OrganizationWalletHistoryDao dao;

    @Test
    public void testFindMoneyTotalByOrg() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        Calendar now = Calendar.getInstance();

        List<SettlementOrgSumData> moneyTotalByOrg = dao.findMoneyTotalByOrg(now, 2014, 3, ids);

        assertNotNull(moneyTotalByOrg);
    }
}