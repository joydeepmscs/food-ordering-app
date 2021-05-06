package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.dao.OrderDao;
import com.upgrad.foodorderingapp.service.entity.CouponEntity;
import com.upgrad.foodorderingapp.service.entity.OrderEntity;
import com.upgrad.foodorderingapp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.*;

public class OrderService {

    @Autowired
    private OrderDao orderDao;

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
}
