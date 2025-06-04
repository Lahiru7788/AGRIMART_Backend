package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;

public interface SupermarketSeedsOrderService {

   SupermarketSeedsOrderAddResponse saveOrUpdate(SupermarketSeedsOrderDto supermarketSeedsOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerSeedsOrderByProductId(int productID);
    SupermarketSeedsOrderGetResponse getSupermarketSeedsOrderByUserId(int userID);
    SupermarketSeedsOrderDeleteResponse DeleteSupermarketSeedsOrderResponse(int orderID);
    SupermarketSeedsOrderGetResponse getSupermarketSeedsOrdersByProductId(int productID);
    //    ConsumerSeedsOrderGetResponse getSupermarketSeedsOrderBySeedsSellerUserId(int userID);
    SupermarketSeedsOrderConfirmResponse ConfirmSupermarketSeedsOrderResponse(int orderID);
    SupermarketSeedsOrderRejectResponse RejectSupermarketSeedsOrderResponse(int orderID);
    SupermarketSeedsOrderGetResponse getSupermarketSeedsOrderBySeedsSellerUserId(int userID);
    SupermarketSeedsOrderAddToCartResponse AddToCartSupermarketSeedsOrderResponse(int orderID);
    SupermarketSeedsOrderRemovedFromCartResponse RemovedFromCartSupermarketSeedsOrderResponse(int orderID);
    SupermarketSeedsPaymentResponse PaymentSupermarketSeedsOrderResponse(int orderID);
}
