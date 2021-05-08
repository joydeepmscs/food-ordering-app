package com.upgrad.foodorderingapp.service.dao;

import com.upgrad.foodorderingapp.service.entity.StateEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {
    private final Logger log = LoggerFactory.getLogger(StateDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to retrieve StateEntity by uuid from db
     *
     * @param stateUuid
     * @return
     */
    public StateEntity getStateByUUID(final String stateUuid) {
        try {
            return entityManager.createNamedQuery("stateByUUID", StateEntity.class)
                    .setParameter("stateUuid", stateUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            log.info("State with uuid doesn't exist in db: {}", stateUuid);
            return null;
        }
    }

    /**
     * Method to retrieve all states from the database
     *
     * @param
     * @return
     */
    public List<StateEntity> getAllStates() {
        return entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
    }
}
