package com.borsam.repository.dao.wf.impl;

import com.borsam.repository.dao.wf.ActDao;
import com.borsam.repository.entity.wf.Act;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Dao - 活动环节
 * Created by Sebarswee on 2015/7/3.
 */
@Repository("actDaoImpl")
public class ActDaoImpl extends BaseDaoImpl<Act, Long> implements ActDao {

    @Override
    public List<Act> getActList(String wfCode) {
        String jpql = "select act from Act act where act.wfCode = :wfCode";

        TypedQuery<Act> query = this.entityManager.createQuery(jpql, Act.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("wfCode", wfCode);
        return query.getResultList();
    }

    @Override
    public Act getStartAct(String wfCode) {
        String jpql = "select act from Act act where act.wfCode = :wfCode and act.type = :type";

        TypedQuery<Act> query = this.entityManager.createQuery(jpql, Act.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("wfCode", wfCode).setParameter("type", Act.Type.start);
        return query.getSingleResult();
    }

    @Override
    public Act getAct(String wfCode, int seq) {
        String jpql = "select act from Act act where act.wfCode = :wfCode and act.seq = :seq";

        TypedQuery<Act> query = this.entityManager.createQuery(jpql, Act.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("wfCode", wfCode).setParameter("seq", seq);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Act getAct(String wfCode, String actCode) {
        String jpql = "select act from Act act where act.wfCode = :wfCode and act.actCode = :actCode";

        TypedQuery<Act> query = this.entityManager.createQuery(jpql, Act.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("wfCode", wfCode).setParameter("actCode", actCode);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
