package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;

public interface ConsumerOrderService {
    ConsumerOrderAddResponse saveOrUpdate(ConsumerOrderDto consumerOrderDto);
//    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerOrderByProductId(int productID);
    ConsumerOrderGetResponse getConsumerOrderByUserId(int userID);
    ConsumerOrderDeleteResponse DeleteConsumerOrderResponse(int orderID);
    ConsumerOrderGetResponse getConsumerOrdersByProductId(int productID);
    ConsumerOrderGetResponse getConsumerOrderByFarmerUserId(int userID);
    ConsumerOrderConfirmResponse ConfirmConsumerOrderResponse(int orderID);
    ConsumerOrderRejectResponse RejectConsumerOrderResponse(int orderID);
    ConsumerOrderAddToCartResponse AddToCartConsumerOrderResponse(int orderID);
    ConsumerOrderRemovedFromCartResponse RemovedFromCartConsumerOrderResponse(int orderID);
    ConsumerOrderPaymentResponse PaymentConsumerOrderResponse(int orderID);

}
