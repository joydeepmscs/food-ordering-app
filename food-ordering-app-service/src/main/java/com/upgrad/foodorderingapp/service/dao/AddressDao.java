package com.upgrad.foodorderingapp.service.dao;

import com.upgrad.foodorderingapp.service.entity.AddressEntity;
import com.upgrad.foodorderingapp.service.entity.CustomerAddressEntity;
import com.upgrad.foodorderingapp.service.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {
    private final Logger log = LoggerFactory.getLogger(AddressDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to save AddressEntity in the database
     *
     * @param addressEntity
     * @return
     */
    public AddressEntity saveAddress(final AddressEntity addressEntity) {
        log.debug("Saving a new address in the db");
        entityManager.persist(addressEntity);
        log.info("address persisted successfully");
        return addressEntity;
    }

    /**
     * Method to retrieve all addresses for a customer
     *
     * @param customerEntity
     * @return
     */
    public List<CustomerAddressEntity> getAllAddress(final CustomerEntity customerEntity) {
        return entityManager
                .createNamedQuery("customerAddressByCustomer", CustomerAddressEntity.class)
                .setParameter("customerId", customerEntity.getId())
                .getResultList();
    }
    /**
     * Method to get AddressEntity by UUID from db
     *
     * @param addressUuid
     * @return
     */
    public AddressEntity getAddressByUUID(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("addressByUuid", AddressEntity.class)
                    .setParameter("addressUuid", addressUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            log.info("No address present with this uuid: {}", addressUuid);
            return null;
        }
    }

    /**
     * Method to retrieve CustomerAddressEntity by customer and address Id
     *
     * @param address
     * @param customer
     * @return
     */
    public CustomerAddressEntity getCustomerAddress(final AddressEntity address,final CustomerEntity customer) {
        try {
            return entityManager.createNamedQuery("customerAddressByCustomerAndAddrId", CustomerAddressEntity.class)
                    .setParameter("customerId", customer.getId())
                    .setParameter("addressId", address.getId())
                    .getSingleResult();
        } catch (NoResultException nre) {
            log.info("No customer address found with the customer and address Id combination: {} ,{} ", customer.getId(),address.getId());
            return null;
        }
    }

    /**
     * Method to soft delete AddressEntity from the db
     *
     * @param addressEntity
     */
    public AddressEntity deleteAddress(final AddressEntity addressEntity) {
        entityManager.merge(addressEntity);
        log.info("Address successfully deleted(soft) from db");
        return addressEntity;
    }
}
