package com.borsam.repository.dao.wf.impl;

import com.borsam.repository.dao.wf.WorkItemDao;
import com.borsam.repository.entity.wf.Act;
import com.borsam.repository.entity.wf.Actor;
import com.borsam.repository.entity.wf.WorkItem;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

/**
 * Dao - 工作项
 * Created by Sebarswee on 2015/7/3.
 */
@Repository("workItemDaoImpl")
public class WorkItemDaoImpl extends BaseDaoImpl<WorkItem, Long> implements WorkItemDao {

    @Override
    public WorkItem findRunningWorkItem(Long guid, Actor.Type type, Long actorId) {
        String jpql = "select w from WorkItem w where w.guid = :guid and w.type = :type and w.actorId = :actorId"
                + " and w.status in :status";

        TypedQuery<WorkItem> query = this.entityManager.createQuery(jpql, WorkItem.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("guid", guid)
                .setParameter("type", type)
                .setParameter("actorId", actorId)
                .setParameter("status", Arrays.asList(WorkItem.Status.running, WorkItem.Status.pending));
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<WorkItem> findUnFinishedWorkItems(Long guid) {
        String jpql = "select w from WorkItem w where w.guid = :guid and w.status in :status";

        TypedQuery<WorkItem> query = this.entityManager.createQuery(jpql, WorkItem.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("guid", guid).setParameter("status", Arrays.asList(WorkItem.Status.running, WorkItem.Status.pending));
        return query.getResultList();
    }

    @Override
    public List<WorkItem> getLastWorkItems(Long guid, Act act) {
        String jpql = "select w from WorkItem w where w.guid = :guid and w.status in :status and w.act = :act and w.exeId is not null";

        TypedQuery<WorkItem> query = this.entityManager.createQuery(jpql, WorkItem.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("guid", guid).setParameter("status", Arrays.asList(WorkItem.Status.finished)).setParameter("act", act);
        return query.getResultList();
    }

    @Override
    public List<Long> queryTodoGuids(String wfCode, Actor.Type type, Long userId) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<WorkItem> root = criteriaQuery.from(WorkItem.class);
        Predicate restrictions;

        criteriaQuery.select(root.get("guid"));
        criteriaQuery.distinct(true);

        // 参与者为当前人员
        restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), type), criteriaBuilder.equal(root.get("actorId"), userId));
        // 查询未完成的工作项 and workItem.status in (running, pendding)
        restrictions = criteriaBuilder.and(restrictions, root.get("status").in(WorkItem.Status.running, WorkItem.Status.pending));
        // 流程代码
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("wfCode"), wfCode));

        // 设置查询条件 where...
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("act")), criteriaBuilder.desc(root.get("id")));

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);

        return typedQuery.getResultList();
    }
}
