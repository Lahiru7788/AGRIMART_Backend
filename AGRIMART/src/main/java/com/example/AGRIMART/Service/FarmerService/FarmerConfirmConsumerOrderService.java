package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface FarmerConfirmConsumerOrderService {

    FarmerConfirmConsumerOrderAddResponse saveOrUpdate(FarmerConfirmConsumerOrderDto farmerConfirmConsumerOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getFarmerSeedsOrderByProductId(int productID);
    FarmerConfirmConsumerOrderGetResponse getFarmerConfirmConsumerOrderByUserId(int userID);
    FarmerConfirmConsumerOrderDeleteResponse DeleteFarmerConfirmConsumerOrderResponse(int confirmOrderID);
    FarmerConfirmConsumerOrderGetResponse getFarmerConfirmConsumerOrderByOrderId(int orderID);
    //    ConsumerSeedsOrderGetResponse getFarmerSeedsOrderBySeedsSellerUserId(int userID);
    FarmerConfirmConsumerOrderGetResponse getFarmerConfirmConsumerOrderByConsumerUserId(int userID);
    FarmerConfirmConsumerOrderPaymentResponse PaymentFarmerConfirmConsumerOrderResponse(int confirmOrderID);
}
