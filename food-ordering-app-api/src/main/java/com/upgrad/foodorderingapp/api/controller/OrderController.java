package com.upgrad.foodorderingapp.api.controller;

import com.upgrad.foodorderingapp.api.model.*;
import com.upgrad.foodorderingapp.service.businness.CustomerService;
import com.upgrad.foodorderingapp.service.businness.OrderService;
import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.entity.*;
import com.upgrad.foodorderingapp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.upgrad.foodorderingapp.service.common.FoodAppUtil.getAccessToken;
import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.*;

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
        return new ResponseEntity<>(orderListCoupon, HttpStatus.OK);
    }

//    @RequestMapping(method= RequestMethod.GET, path="/order", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<CustomerOrderResponse> pastOrdersOfCustomer(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
//        String accessToken = getAccessToken(authorization);
//        CustomerEntity customer = customerService.getCustomer(accessToken);
//
//        List<OrderEntity> pastOrders = orderService.getPastOrders(customer.getUuid());
//        CustomerOrderResponse pastOrderResponse = new CustomerOrderResponse();
//        if (pastOrders != null & !pastOrders.isEmpty()) {
//            pastOrders.forEach(pastOrder -> {
//                OrderList orderList = new OrderList();
//
//                //Setting Bill details
//                orderList.id(UUID.fromString(pastOrder.getUuid()))
//                        .bill(BigDecimal.valueOf(pastOrder.getBill()))
//                        .date(pastOrder.getDate().toString());
//
//                //Setting Coupon details if any applied
//                CouponEntity pastOrderCoupon = pastOrder.getCoupon_id();
//                if (pastOrderCoupon != null) {
//                    OrderListCoupon coupon = new OrderListCoupon();
//                    coupon.id(UUID.fromString(pastOrderCoupon.getUuid()))
//                            .couponName(pastOrderCoupon.getCouponName())
//                            .percent(pastOrderCoupon.getPercent());
//                    orderList.coupon(coupon);
//                }
//
//                //Setting Payment details
//                OrderListPayment payment = new OrderListPayment();
//                payment.id(UUID.fromString(pastOrder.getPayment().getUuid()))
//                        .paymentName(pastOrder.getPayment().getPaymentName());
//                orderList.payment(payment);
//
//                //Setting Customer details
//                OrderListCustomer customerDetails = new OrderListCustomer();
//                customerDetails.id(UUID.fromString(pastOrder.getCustomer().getUuid()))
//                        .firstName(pastOrder.getCustomer().getFirstName())
//                        .lastName(pastOrder.getCustomer().getFirstName())
//                        .emailAddress(pastOrder.getCustomer().getEmailAddress())
//                        .contactNumber(pastOrder.getCustomer().getContactNumber());
//                orderList.customer(customerDetails);
//
//                //Setting Address and State details
//                OrderListAddress orderListAddress = new OrderListAddress();
//                AddressEntity orderAddress = pastOrder.getAddress();
//                orderListAddress.id(UUID.fromString(orderAddress.getUuid()))
//                        .flatBuildingName(orderAddress.getFlatBuilNo())
//                        .locality(orderAddress.getLocality()).city(orderAddress.getCity())
//                        .pincode(orderAddress.getPincode());
//                OrderListAddressState orderListAddressState = new OrderListAddressState();
//                StateEntity orderState = orderAddress.getState();
//                orderListAddressState.id(UUID.fromString(orderState.getStateUuid()))
//                        .stateName(orderState.getStateName());
//                orderListAddress.state(orderListAddressState);
//                orderList.address(orderListAddress);
//
//                //Setting Item details
//                List<ItemEntity> orderItems = orderService.getOrderItemsByOrderId(pastOrder.getId());
//                if (orderItems != null) {
//                    orderItems.stream().forEach(orderItem -> {
//                        ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
//                        itemQuantityResponse.quantity(orderItem.getQuantity()).price(orderItem.getPrice());
//                        ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
//                        itemQuantityResponseItem.id(UUID.fromString(orderItem.getItem().getUuid()))
//                                .itemName(orderItem.getItem().getItemName()).itemPrice(orderItem.getPrice())
//                                .type(ItemQuantityResponseItem.TypeEnum
//                                        .fromValue(orderItem.getItem().getType().getValue()));
//                        itemQuantityResponse.item(itemQuantityResponseItem);
//                        orderList.addItemQuantitiesItem(itemQuantityResponse);
//                    });
//                }
//                if (orderList.getItemQuantities() == null) {
//                    orderList.setItemQuantities(new ArrayList<ItemQuantityResponse>());
//                }
//                pastOrderResponse.addOrdersItem(orderList);
//
//            });
//        }
//        if (pastOrderResponse.getOrders() == null) {
//            pastOrderResponse.setOrders(new ArrayList<OrderList>());
//        }
//        return new ResponseEntity<CustomerOrderResponse>(pastOrderResponse, HttpStatus.OK);
//    }

//    @RequestMapping(method=RequestMethod.POST, path="/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String authorization, @RequestBody SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, AddressNotFoundException, CouponNotFoundException, ItemNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException {
//        String accessToken = getAccessToken(authorization);
//        customerService.getCustomer(accessToken);
//
//        CouponEntity coupon = null;
//        PaymentEntity payment = null;
//        AddressEntity address = null;
//        RestaurantEntity restaurant = null;
//        if(saveOrderRequest.getCouponId() != null) {
//            coupon = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
//        }
//        if(saveOrderRequest.getPaymentId() != null) {
//            payment = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
//        }
//        else {
//            throw new PaymentMethodNotFoundException(PNF_002.getCode(), PNF_002.getDefaultMessage());
//        }
//        if (saveOrderRequest.getAddressId() != null) {
//            address = addressService.getAddressByUUID(saveOrderRequest.getAddressId(), loggedInCustomer);
//        }
//        else {
//            throw new AddressNotFoundException(ANF_003.getCode(), ANF_003.getDefaultMessage());
//        }
//        if (saveOrderRequest.getRestaurantId() != null) {
//            restaurant = restaurantService
//                    .restaurantByUUID(saveOrderRequest.getRestaurantId().toString());
//        }
//        else {
//            throw new RestaurantNotFoundException(RNF_001.getCode(), RNF_001.getDefaultMessage());
//        }
//        OrderEntity order = new OrderEntity();
//        order.setCustomer(loggedInCustomer);
//        order.setCoupon(coupon);
//        order.setAddress(address);
//        order.setPayment(payment);
//        order.setRestaurant(restaurant);
//        order.setBill(saveOrderRequest.getBill() != null ?
//                saveOrderRequest.getBill().doubleValue() : null);
//        order.setDiscount(saveOrderRequest.getDiscount() != null ?
//                saveOrderRequest.getDiscount().doubleValue() : null);
//        final OrderEntity savedOrder = orderService.saveOrder(order);
//
//        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
//        if (itemQuantities != null && !itemQuantities.isEmpty()) {
//            for (ItemQuantity itemQuantity : itemQuantities) {
//                ItemEntity item = itemService.getItemByUUID(String.valueOf(itemQuantity.getItemId()));
//                OrderItemEntity orderItem = new OrderItemEntity();
//                orderItem.setItem(item);
//                orderItem.setOrder(savedOrder);
//                orderItem.setQuantity(itemQuantity.getQuantity());
//                orderItem.setPrice(itemQuantity.getPrice());
//                orderService.saveOrderItem(orderItem);
//            }
//        }
//
//        SaveOrderResponse response = new SaveOrderResponse();
//        response.id(savedOrder.getUuid()).status("ORDER SUCCESSFULLY PLACED");
//        return new ResponseEntity<SaveOrderResponse>(response, HttpStatus.CREATED);
//    }
}
