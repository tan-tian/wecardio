package com.borsam.service.wf.impl;

import com.borsam.pub.UserType;
import com.borsam.repository.dao.wf.ActDao;
import com.borsam.repository.dao.wf.WorkItemDao;
import com.borsam.repository.entity.wf.Act;
import com.borsam.repository.entity.wf.Actor;
import com.borsam.repository.entity.wf.WorkItem;
import com.borsam.service.wf.QueryService;
import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.web.I18Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Service - 流程查询服务
 * Created by Sebarswee on 2015/7/7.
 */
@Service("queryServiceImpl")
public class QueryServiceImpl implements QueryService {

    @Resource(name = "actDaoImpl")
    private ActDao actDao;

    @Resource(name = "workItemDaoImpl")
    private WorkItemDao workItemDao;

    @Transactional(readOnly = true)
    @Override
    public List<Map<String, String>> getFlowInfo(String wfCode, Long guid) {
        List<Filter> filters = new ArrayList<Filter>();
        List<Order> orders = new ArrayList<Order>();

        filters.add(Filter.eq("guid", guid));
        orders.add(Order.asc("createDate"));

        List<WorkItem> traceList = workItemDao.findList(null, null, filters, orders);
        List<WorkItem> temp = new LinkedList<WorkItem>();
        for (WorkItem workItem : traceList) {
            if (temp.isEmpty()) {
                temp.add(workItem);
            } else {
                WorkItem cur = temp.get(temp.size() - 1);
                if (workItem.getAct().getSeq() < cur.getAct().getSeq()) {
                    temp.remove(temp.size() - 1);
                } else {
                    temp.add(workItem);
                }
            }
        }

        List<Act> actList = actDao.getActList(wfCode);
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        int no = 1;
        for (Act act :  actList) {
            // 开始活动不显示
            if (act.getType().equals(Act.Type.start)) {
                continue;
            }
            Map<String, String> map = new HashMap<String, String>();

            if (no == 1) {
                map.put("cls", "flowStart");
            } else if (act.getType().equals(Act.Type.end)) {
                map.put("cls", "flowEnd");
            }
            map.put("no", (no++) + ".");
            map.put("name", I18Util.getMessage(act.getActName()));  // 国际化

            if (act.getType().equals(Act.Type.end)) {
                // 结束环节，判断最后一个工作项状态是否为完成
                WorkItem last = temp.get(temp.size() - 1);
                Act a = last.getAct();
                if (a.getSeq() + 1 == act.getSeq() && last.getStatus().equals(WorkItem.Status.finished)) {
                    map.put("cls", "flowEnd flowEndOn");
                }
            } else {
                for (WorkItem w : temp) {
                    Act a = w.getAct();
                    if (a.equals(act) && no != 2) {
                        map.put("cls", "flowOn");
                        break;
                    }
                }
            }

            result.add(map);
        }

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkItem> queryProcessInfo(Long guid, Boolean showAll) {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(Filter.eq("guid", guid));
        filters.add(Filter.in("status", new Object[] { WorkItem.Status.finished, WorkItem.Status.forceEnd, WorkItem.Status.cancel }));
        filters.add(Filter.isNotNull("exeId"));
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.asc("createDate"));
        // 已完成工作项
        List<WorkItem> finished = workItemDao.findList(null, null, filters, orders);

        if (showAll) {
            // 未完成工作项
            filters = new ArrayList<Filter>();
            filters.add(Filter.eq("guid", guid));
            filters.add(Filter.in("status", new Object[] { WorkItem.Status.running }));
            List<WorkItem> unfinished = workItemDao.findList(null, null, filters, orders);
            if (unfinished != null && !unfinished.isEmpty()) {
    //            for (WorkItem w : unfinished) {
    //                w.setExeStaffName(w.getActorName());
    //            }
                finished.addAll(unfinished);
            }
        }
        return finished;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Long> queryTodoGuids(String wfCode, UserType userType, Long userId) {
        Actor.Type type = null;
        switch (userType) {
            case admin:
                type = Actor.Type.admin;
                break;
            case org:
                type = Actor.Type.organization;
                break;
            case doctor:
                type = Actor.Type.doctor;
                break;
            case patient:
                type = Actor.Type.patient;
                break;
        }
        return workItemDao.queryTodoGuids(wfCode, type, userId);
    }
}
