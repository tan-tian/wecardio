package com.hiteam.baseTest;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * Description:
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2014-09-23 20:01
 * </pre>
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContextDao_test.xml"})
public class BaseDaoTest {
    public Logger logger = LoggerFactory.getLogger(this.getClass());

}
