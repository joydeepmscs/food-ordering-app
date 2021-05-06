package com.upgrad.foodorderingapp.service.dao;

import com.upgrad.foodorderingapp.service.entity.CouponEntity;
import com.upgrad.foodorderingapp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponByCouponName(String couponName) {
        try {
            List<CouponEntity> couponList = entityManager.createNamedQuery("couponByCouponName", CouponEntity.class).setParameter("couponName", couponName).getResultList();
            if(couponList != null && !couponList.isEmpty()) {
                return couponList.get(0);
            }
            else {
                return null;
            }
        }
        catch(NoResultException nre) {
            return null;
        }
    }

    public List<OrderEntity> getPastOrders(String customerUUID) {
        return entityManager.createNamedQuery("pastOrdersByCustomer", OrderEntity.class).setParameter("customerUUID", customerUUID).getResultList();
    }
}
