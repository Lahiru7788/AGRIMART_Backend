package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmSupermarketOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface FarmerConfirmSupermarketOrderService {

    FarmerConfirmSupermarketOrderAddResponse saveOrUpdate(FarmerConfirmSupermarketOrderDto farmerConfirmSupermarketOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getFarmerSeedsOrderByProductId(int productID);
    FarmerConfirmSupermarketOrderGetResponse getFarmerConfirmSupermarketOrderByUserId(int userID);
    FarmerConfirmSupermarketOrderDeleteResponse DeleteFarmerConfirmSupermarketOrderResponse(int confirmOrderID);
    FarmerConfirmSupermarketOrderGetResponse getFarmerConfirmSupermarketOrderByOrderId(int orderID);
    //    ConsumerSeedsOrderGetResponse getFarmerSeedsOrderBySeedsSellerUserId(int userID);
    FarmerConfirmSupermarketOrderGetResponse getFarmerConfirmSupermarketOrderBySupermarketUserId(int userID);
    FarmerConfirmSupermarketOrderPaymentResponse PaymentFarmerConfirmSupermarketOrderResponse(int confirmOrderID);
}
