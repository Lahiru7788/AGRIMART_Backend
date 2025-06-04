package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerHireDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerHireDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface ConsumerHireService {

   ConsumerHireAddResponse saveOrUpdate(ConsumerHireDto consumerHireDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerOrderByProductId(int productID);
    ConsumerHireGetResponse getConsumerHireByUserId(int userID);
    ConsumerHireDeleteResponse DeleteConsumerHireResponse(int orderID);
    ConsumerHireGetResponse getConsumerHireByHireId(int HireID);
    ConsumerHireGetResponse getConsumerHireByTrainerUserId(int userID);
    ConsumerHireConfirmResponse ConfirmConsumerHireResponse(int orderID);
    ConsumerHireRejectResponse RejectConsumerHireResponse(int orderID);
    ConsumerHireAddToCartResponse AddToCartConsumerHireResponse(int orderID);
    ConsumerHireRemovedFromCartResponse RemovedFromCartConsumerHireResponse(int orderID);
    ConsumerHirePaymentResponse PaymentConsumerHireResponse(int orderID);
}
