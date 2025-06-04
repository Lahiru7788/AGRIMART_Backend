package com.example.AGRIMART.Service.SFService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SFResponse.*;

public interface SFOrderService {

    SFOrderAddResponse saveOrUpdate(SFOrderDto sfOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerOrderByProductId(int productID);
    SFOrderGetResponse getSFOrderByUserId(int userID);
    SFOrderDeleteResponse DeleteSFOrderResponse(int orderID);
    SFOrderGetResponse getSFOrdersByProductId(int productID);
    SFOrderGetResponse getSFOrderByFarmerUserId(int userID);
    SFOrderConfirmResponse ConfirmSFOrderResponse(int orderID);
    SFOrderRejectResponse RejectSFOrderResponse(int orderID);
    SFOrderAddToCartResponse AddToCartSFOrderResponse(int orderID);
    SFOrderRemovedFromCartResponse RemovedFromCartSFOrderResponse(int orderID);
    SFOrderPaymentResponse PaymentSFOrderResponse(int orderID);
}
