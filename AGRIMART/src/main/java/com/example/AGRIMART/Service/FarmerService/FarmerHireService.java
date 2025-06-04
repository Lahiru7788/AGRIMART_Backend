package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerHireDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface FarmerHireService {

    FarmerHireAddResponse saveOrUpdate(FarmerHireDto farmerHireDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerOrderByProductId(int productID);
    FarmerHireGetResponse getFarmerHireByUserId(int userID);
    FarmerHireDeleteResponse DeleteFarmerHireResponse(int orderID);
    FarmerHireGetResponse getFarmerHireByHireId(int HireID);
    FarmerHireGetResponse getFarmerHireByTrainerUserId(int userID);
    FarmerHireConfirmResponse ConfirmFarmerHireResponse(int orderID);
    FarmerHireRejectResponse RejectFarmerHireResponse(int orderID);
    FarmerHireAddToCartResponse AddToCartFarmerHireResponse(int orderID);
    FarmerHireRemovedFromCartResponse RemovedFromCartFarmerHireResponse(int orderID);
    FarmerHirePaymentResponse PaymentFarmerHireResponse(int orderID);

}
