package com.upgrad.foodorderingapp.service.dao;

import com.upgrad.foodorderingapp.service.entity.CustomerAuthEntity;
import com.upgrad.foodorderingapp.service.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    private final Logger log = LoggerFactory.getLogger(CustomerDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity createCustomer(final CustomerEntity customerEntity) {
        log.debug("creating a new customer in the db");
        entityManager.persist(customerEntity);
        log.info("customer persisted successfully");
        return customerEntity;
    }

    /**
     * Get Customer based on contact number
     *
     * @param contactNumber
     * @return
     */
    public CustomerEntity getCustomerByContactNo(final String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class)
                    .setParameter("contactNo", contactNumber)
                    .getSingleResult();
        } catch (NoResultException nre) {
            log.error("customer with this contactNo doesn't exist in db: {}",contactNumber);
            return null;
        }
    }
    /**
     * Method to persist CustomerAuthEntity in the database
     *
     * @param customerAuth - CustomerAuthEntity to be persisted in the database
     *
     */
    public void createAuthToken(final CustomerAuthEntity customerAuth) {
        entityManager.persist(customerAuth);
    }

    /**
     * retrieve CustomerAuthEntity based on access token
     *
     * @param accessToken
     * @return
     */
    public CustomerAuthEntity getCustomerAuthEntity(final String accessToken) {
        try {
            return entityManager.createNamedQuery("customerAuthByAccessToken", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            log.error("customer with this accessToken doesn't exist in db");
            return null;
        }
    }

    /**
     * update CustomerAuthEntity
     *
     * @param customerAuthEntity
     */
    public void updateCustomerAuth(final CustomerAuthEntity customerAuthEntity) {
        entityManager.merge(customerAuthEntity);
    }

    /**
     * Method to update CustomerEntity
     *
     * @param customerEntity
     * @return
     */
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity) {
        return entityManager.merge(customerEntity);
    }
}
