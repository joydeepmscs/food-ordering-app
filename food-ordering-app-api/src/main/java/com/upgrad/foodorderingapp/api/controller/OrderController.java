package com.upgrad.foodorderingapp.api.controller;

import com.upgrad.foodorderingapp.api.model.CustomerOrderResponse;
import com.upgrad.foodorderingapp.api.model.OrderListCoupon;
import com.upgrad.foodorderingapp.service.businness.CustomerService;
import com.upgrad.foodorderingapp.service.businness.OrderService;
import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.entity.CouponEntity;
import com.upgrad.foodorderingapp.service.entity.CustomerEntity;
import com.upgrad.foodorderingapp.service.entity.OrderEntity;
import com.upgrad.foodorderingapp.service.exception.AuthorizationFailedException;
import com.upgrad.foodorderingapp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.upgrad.foodorderingapp.service.common.FoodAppUtil.getAccessToken;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method= RequestMethod.GET, path="/order/coupon/{coupon_name}", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OrderListCoupon> couponsByCouponName(@RequestHeader("authorization") final String authorization, @PathVariable(name="coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        String accessToken = getAccessToken(authorization);
        customerService.getCustomer(accessToken);

        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        OrderListCoupon orderListCoupon = new OrderListCoupon();
        orderListCoupon.id(UUID.fromString(couponEntity.getUuid())).couponName(couponEntity.getCouponName()).percent(couponEntity.getPercent());
        return new ResponseEntity<OrderListCoupon>(orderListCoupon, HttpStatus.OK);
    }

//    public ResponseEntity<CustomerOrderResponse> pastOrdersOfCustomer(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
//        String accessToken = getAccessToken(authorization);
//        CustomerEntity customer = customerService.getCustomer(accessToken);
//
//        List<OrderEntity> pastOrders = orderService.getPastOrders(customer.getUuid());
//        CustomerOrderResponse pastOrderResponse = new CustomerOrderResponse();
//        if(pastOrders != null & !pastOrders.isEmpty()) {
//
//        }
//    }
}
