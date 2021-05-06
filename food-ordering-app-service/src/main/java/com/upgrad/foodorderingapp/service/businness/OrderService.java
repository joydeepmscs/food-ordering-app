package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.dao.OrderDao;
import com.upgrad.foodorderingapp.service.entity.CouponEntity;
import com.upgrad.foodorderingapp.service.entity.ItemEntity;
import com.upgrad.foodorderingapp.service.entity.OrderEntity;
import com.upgrad.foodorderingapp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.*;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

//    @Autowired
//    private ItemDao itemDao;

    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {
        if(FoodAppUtil.isEmptyField(couponName)) {
            throw new CouponNotFoundException(CPF_002.getCode(), CPF_002.getDefaultMessage());
        }
        CouponEntity couponEntity = orderDao.getCouponByCouponName(couponName);
        if(couponEntity == null) {
            throw new CouponNotFoundException(CPF_001.getCode(), CPF_001.getDefaultMessage());
        }
        return couponEntity;
    }

    public List<OrderEntity> getPastOrders(String customerUUID) {
        return orderDao.getPastOrders(customerUUID);
    }

//    public List<ItemEntity> getOrderItemsByOrderId(Integer orderId) {
//        return itemDao.getItemsByOrderId(orderId);
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(OrderEntity order) {
        order.setUuid(UUID.randomUUID().toString());
        order.setDate(new Date());
        // Set the status of address to archive
//        order.getAddress().setActive(0);
        orderDao.saveOrderDetail(order);
        return order;
    }
}
