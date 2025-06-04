package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface FarmerSeedsOrderService {

    FarmerSeedsOrderAddResponse saveOrUpdate(FarmerSeedsOrderDto farmerSeedsOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getFarmerSeedsOrderByProductId(int productID);
    FarmerSeedsOrderGetResponse getFarmerSeedsOrderByUserId(int userID);
    FarmerSeedsOrderDeleteResponse DeleteFarmerSeedsOrderResponse(int orderID);
    FarmerSeedsOrderGetResponse getFarmerSeedsOrdersByProductId(int productID);
    //    ConsumerSeedsOrderGetResponse getFarmerSeedsOrderBySeedsSellerUserId(int userID);
    FarmerSeedsOrderConfirmResponse ConfirmFarmerSeedsOrderResponse(int orderID);
    FarmerSeedsOrderRejectResponse RejectFarmerSeedsOrderResponse(int orderID);
    FarmerSeedsOrderGetResponse getFarmerSeedsOrderBySeedsSellerUserId(int userID);
    FarmerSeedsOrderAddToCartResponse AddToCartFarmerSeedsOrderResponse(int orderID);
    FarmerSeedsOrderRemovedFromCartResponse RemovedFromCartFarmerSeedsOrderResponse(int orderID);
    FarmerSeedsOrderPaymentResponse PaymentFarmerSeedsOrderResponse(int orderID);
}
