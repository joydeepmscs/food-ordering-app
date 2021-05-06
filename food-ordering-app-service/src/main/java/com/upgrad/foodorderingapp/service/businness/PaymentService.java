package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.dao.PaymentDao;
import com.upgrad.foodorderingapp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    public List<PaymentEntity> getAllPaymentMethods() {
        return paymentDao.getAllPaymentMethods();
    }
}
