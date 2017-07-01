package com.borsam.repository.dao.wf.impl;

import com.borsam.repository.dao.wf.ActorDao;
import com.borsam.repository.entity.wf.Actor;
import com.hiteam.common.base.repository.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Dao - 活动参与者
 * Created by Sebarswee on 2015/7/3.
 */
@Repository("actorDaoImpl")
public class ActorDaoImpl extends BaseDaoImpl<Actor, Long> implements ActorDao {

    @Override
    public List<Actor> findActor(String wfCode, String actCode) {
        String jpql = "select actor from Actor actor where actor.wfCode = :wfCode and actor.act.actCode = :actCode";

        TypedQuery<Actor> query = this.entityManager.createQuery(jpql, Actor.class).setFlushMode(FlushModeType.COMMIT);
        query.setParameter("wfCode", wfCode).setParameter("actCode", actCode);
        return query.getResultList();
    }
}
