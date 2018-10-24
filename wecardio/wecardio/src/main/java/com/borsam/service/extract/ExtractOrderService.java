package com.borsam.service.extract;

import com.borsam.repository.entity.extract.ExtractOrder;
import com.hiteam.common.base.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Service - 提现单据
 * Created by tantian on 2015/8/10.
 */
public interface ExtractOrderService extends BaseService<ExtractOrder, Long> {

    /**
     * 提现申请
     * @param order 提现单据
     */
    public void create(ExtractOrder order);

    /**
     * 重新提交申请
     * @param order 提现单据
     */
    public void reCreate(ExtractOrder order);

    /**
     * 审核
     * @param orderId 单据标识
     * @param isPass 是否通过
     * @param remark 备注信息
     */
    public void audit(Long orderId, Boolean isPass, String remark);

    /**
     * 出账
     * @param orderId 单据标识
     * @param bankSeq 银行流水号
     * @param remark 备注信息
     */
    public void confirm(Long orderId, String bankSeq, String remark);

    /**
     * 删除提现月结信息
     * @param orderId 单据标识
     * @param datas 删除的年月信息，数据结构：[ { "year" : "2015", "month", "7" }, { "year" : "2015", "month", "8" } ]
     */
    public void removeSettlement(Long orderId, List<Map> datas);

    /**
     * 新增提现月结信息
     * @param orderId 单据标识
     * @param datas 删除的年月信息，数据结构：[ { "year" : "2015", "month", "7" }, { "year" : "2015", "month", "8" } ]
     */
    public void addSettlement(Long orderId, List<Map> datas);
}
