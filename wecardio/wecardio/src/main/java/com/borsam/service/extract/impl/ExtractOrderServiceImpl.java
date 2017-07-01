package com.borsam.service.extract.impl;

import com.borsam.pojo.wallet.Settlement;
import com.borsam.pojo.wf.CreateProcessParam;
import com.borsam.pojo.wf.WfCode;
import com.borsam.repository.dao.extract.ExtractOrderDao;
import com.borsam.repository.dao.extract.MonthClearDao;
import com.borsam.repository.dao.extract.OutInfoDao;
import com.borsam.repository.dao.org.OrganizationWalletDao;
import com.borsam.repository.dao.org.OrganizationWalletHistoryDao;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.extract.ExtractOrder;
import com.borsam.repository.entity.extract.MonthClear;
import com.borsam.repository.entity.extract.OutInfo;
import com.borsam.repository.entity.org.Organization;
import com.borsam.repository.entity.org.OrganizationWallet;
import com.borsam.service.admin.AdminService;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.extract.ExtractOrderService;
import com.borsam.service.pub.SnService;
import com.borsam.service.wf.ProcessEngine;
import com.hiteam.common.base.service.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service - 提现单据
 * Created by Sebarswee on 2015/8/10.
 */
@Service("extractOrderServiceImpl")
public class ExtractOrderServiceImpl extends BaseServiceImpl<ExtractOrder, Long> implements ExtractOrderService {

    @Resource(name = "processEngineImpl")
    private ProcessEngine processEngine;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "monthClearDaoImpl")
    private MonthClearDao monthClearDao;

    @Resource(name = "outInfoDaoImpl")
    private OutInfoDao outInfoDao;

    @Resource(name = "organizationWalletDaoImpl")
    private OrganizationWalletDao organizationWalletDao;

    @Resource(name = "organizationWalletHistoryDaoImpl")
    private OrganizationWalletHistoryDao organizationWalletHistoryDao;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "extractOrderDaoImpl")
    public void setBaseDao(ExtractOrderDao extractOrderDao) {
        super.setBaseDao(extractOrderDao);
    }

    @Override
    @Transactional
    public void create(ExtractOrder order) {
        DoctorAccount doctorAccount = doctorAccountService.getCurrent();
        DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
        Organization organization = doctorProfile.getOrg();

        List<MonthClear> monthClears = order.getMonthClears();
        for (MonthClear monthClear : monthClears) {
            Settlement settlement = organizationWalletHistoryDao.findUnSettlement(organization.getId(), monthClear.getYear(), monthClear.getMonth());
            if (settlement == null || settlement.getMoney().compareTo(monthClear.getApplyMoney()) != 0) {
                // 根据提现年月统计流水记录，防止恶意提现，数据不匹配则为非法操作
                throw new RuntimeException("非法数据");
            }
            monthClear.setOrder(order);
            // 更新机构钱包流水记录的状态，防止被重复提取
            organizationWalletHistoryDao.updateState(organization.getId(), monthClear.getYear(), monthClear.getMonth(), 1, 2);
        }

        BigDecimal total = this.calculateMoney(order);
        if (total.compareTo(new BigDecimal(0)) == 0) {
            throw new RuntimeException("提现金额为0");
        }

        // 启动提现流程
        CreateProcessParam processParam = new CreateProcessParam();
        processParam.setWfCode(WfCode.WITHDRAW);
        processParam.setIsPush(true);
        processParam.setDatas(new HashMap<String, String>() {
            {
                put("[WorkItem].result", "common.wf.result.create");
            }
        });
        Long guid = processEngine.startProcessInstance(processParam);

        // 设置提现单据信息
        order.setOrg(organization);
        order.setOrderNo("");
        order.setMoney(total);
        order.setState(1);  // 1-待审核
        order.setCreateId(doctorProfile.getId());
        order.setCreateName(doctorProfile.getName());
        order.setGuid(guid);
        this.save(order);

        // 保存出账账户信息
        OutInfo outInfo = order.getOutInfo();
        outInfo.setId(order.getId());
        outInfoDao.persist(outInfo);

        // 保存月结信息
        monthClearDao.persist(monthClears);

        // 生成单据编号
        order.setOrderNo(snService.generate(order.getId()));
        this.update(order);
    }

    @Override
    @Transactional
    public void reCreate(ExtractOrder order) {
        Long guid = order.getGuid();
        processEngine.pushProcessInstance(guid, null, new HashMap<String, String>() {
            {
                put("[WorkItem].result", "common.wf.result.reCreate");
                put("[WorkItem].remark", "");
            }
        });
        order.setState(1);  // 1-待审核
        this.update(order);
    }

    /**
     * 计算总提现金额
     * @param order 提现单据
     * @return 提现金额
     */
    private BigDecimal calculateMoney(ExtractOrder order) {
        BigDecimal total = new BigDecimal(0);
        for (MonthClear monthClear : order.getMonthClears()) {
            total = total.add(monthClear.getApplyMoney());
        }
        return total;
    }

    @Override
    @Transactional
    public void audit(Long orderId, Boolean isPass, String remark) {
        ExtractOrder order = this.find(orderId);
        Long guid = order.getGuid();

        if (isPass) {
            processEngine.pushProcessInstance(guid, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.pass");
                    put("[WorkItem].remark", remark);
                }
            });
            order.setState(2);
        } else {
            processEngine.backProcessInstance(guid, null, new HashMap<String, String>() {
                {
                    put("[WorkItem].result", "common.wf.result.noPass");
                    put("[WorkItem].remark", remark);
                }
            });
            order.setState(0);
        }
        this.update(order);
    }

    @Override
    @Transactional
    public void confirm(Long orderId, String bankSeq, String remark) {
        Admin admin = adminService.getCurrent();
        ExtractOrder order = this.find(orderId);
        Long guid = order.getGuid();

        processEngine.pushProcessInstance(guid, null, new HashMap<String, String>() {
            {
                put("[WorkItem].result", "");
                put("[WorkItem].remark", remark);
            }
        });
        // 单据归档
        order.setState(3);

        // 出账信息
        OutInfo outInfo = order.getOutInfo();
        outInfo.setBankSeq(StringUtils.trimToEmpty(bankSeq));
        outInfo.setCreateId(admin.getId());
        outInfo.setCreateName(admin.getName());
        outInfo.setOutTime(new Date().getTime() / 1000);
        outInfo.setMoney(order.getMoney());
        outInfoDao.merge(outInfo);

        // 更新月结信息
        List<MonthClear> monthClears = order.getMonthClears();
        for (MonthClear monthClear : monthClears) {
            monthClear.setOutMoney(monthClear.getApplyMoney());
            monthClear.setOutTime(new Date().getTime() / 1000);
            organizationWalletHistoryDao.updateState(order.getOrg().getId(), monthClear.getYear(), monthClear.getMonth(), 2, 0);
        }
        monthClearDao.merge(monthClears);

        // 更新钱包
        OrganizationWallet organizationWallet = organizationWalletDao.find(order.getOrg().getId());
        organizationWallet.setRealTotal(organizationWallet.getRealTotal().subtract(outInfo.getMoney()));
        organizationWallet.setGrandTotal(organizationWallet.getGrandTotal().add(outInfo.getMoney()));
        organizationWalletDao.merge(organizationWallet);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void removeSettlement(Long orderId, List<Map> datas) {
        ExtractOrder order = this.find(orderId);
        Organization organization = order.getOrg();

        for (Map<String, String> data : datas) {
            int year = Integer.parseInt(data.get("year"));
            int month = Integer.parseInt(data.get("month"));

            // 通过年月查询单据的月结记录，如果不存在则为非法数据，不予操作
            MonthClear monthClear = monthClearDao.findByYearMonth(orderId, year, month);
            if (monthClear == null) {
                throw new RuntimeException("非法数据");
            } else {
                // 删除月结信息
                monthClearDao.remove(monthClear);
                // 更新流水记录状态
                organizationWalletHistoryDao.updateState(organization.getId(), year, month, 2, 1);
            }
            // 减少提现金额
            order.setMoney(order.getMoney().subtract(monthClear.getApplyMoney()));
        }
        this.update(order);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void addSettlement(Long orderId, List<Map> datas) {
        ExtractOrder order = this.find(orderId);
        Organization organization = order.getOrg();

        for (Map<String, String> data : datas) {
            int year = Integer.parseInt(data.get("year"));
            int month = Integer.parseInt(data.get("month"));
            BigDecimal money = new BigDecimal(data.get("money"));

            // 通过年月查询单据的月结记录，如果不存在则为非法数据，不予操作
            Settlement settlement = organizationWalletHistoryDao.findUnSettlement(organization.getId(),year, month);
            if (settlement == null || settlement.getMoney().compareTo(money) != 0) {
                // 根据提现年月统计流水记录，防止恶意提现，数据不匹配则为非法操作
                throw new RuntimeException("非法数据");
            }
            // 新增提现记录
            MonthClear monthClear = new MonthClear();
            monthClear.setOrder(order);
            monthClear.setYear(year);
            monthClear.setMonth(month);
            monthClear.setApplyMoney(money);
            monthClearDao.persist(monthClear);
            // 更新流水记录状态
            organizationWalletHistoryDao.updateState(organization.getId(), year, month, 1, 2);
            // 增加提现金额
            order.setMoney(order.getMoney().add(money));
        }
        this.update(order);
    }
}
