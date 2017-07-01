package com.borsam.service.statistics.impl;

import com.borsam.service.statistics.StatisticService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * <pre>
 * @Description:
 * @author :Zhang zhongtao
 * @version: Ver 1.0
 * @Date: 2015-08-16 21:31
 * </pre>
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class StatisticServiceImplTest {

    @Resource(name = "statisticServiceImpl")
    private StatisticService statisticService;

    @Test
    public void testSumByWallet() throws Exception {
        statisticService.sumByWallet();
    }
}