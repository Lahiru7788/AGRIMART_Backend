package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;

public interface ConsumerSeedsOrderService {
    ConsumerSeedsOrderAddResponse saveOrUpdate(ConsumerSeedsOrderDto consumerOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerSeedsOrderByProductId(int productID);
    ConsumerSeedsOrderGetResponse getConsumerSeedsOrderByUserId(int userID);
    ConsumerSeedsOrderDeleteResponse DeleteConsumerSeedsOrderResponse(int orderID);
    ConsumerSeedsOrderGetResponse getConsumerSeedsOrdersByProductId(int productID);
//    ConsumerSeedsOrderGetResponse getConsumerSeedsOrderBySeedsSellerUserId(int userID);
    ConsumerSeedsOrderConfirmResponse ConfirmConsumerSeedsOrderResponse(int orderID);
    ConsumerSeedsOrderRejectResponse RejectConsumerSeedsOrderResponse(int orderID);
    ConsumerSeedsOrderGetResponse getConsumerSeedsOrderBySeedsSellerUserId(int userID);
    ConsumerSeedsOrderAddToCartResponse AddToCartConsumerSeedsOrderResponse(int orderID);
    ConsumerSeedsOrderRemovedFromCartResponse RemovedFromCartConsumerSeedsOrderResponse(int orderID);
    ConsumerSeedsOrderPaymentResponse PaymentConsumerSeedsOrderResponse(int orderID);

}
