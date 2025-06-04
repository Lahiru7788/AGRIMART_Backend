package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;

public interface TrainerOrderService {

    TrainerOrderAddResponse saveOrUpdate(TrainerOrderDto trainerOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerOrderByProductId(int productID);
    TrainerOrderGetResponse getTrainerOrderByUserId(int userID);
    TrainerOrderDeleteResponse DeleteTrainerOrderResponse(int orderID);
    TrainerOrderGetResponse getTrainerOrdersByProductId(int productID);
    TrainerOrderGetResponse getTrainerOrderByFarmerUserId(int userID);
    TrainerOrderConfirmResponse ConfirmTrainerOrderResponse(int orderID);
    TrainerOrderRejectResponse RejectTrainerOrderResponse(int orderID);
    TrainerOrderAddToCartResponse AddToCartTrainerOrderResponse(int orderID);
    TrainerOrderRemovedFromCartResponse RemovedFromCartTrainerOrderResponse(int orderID);
    TrainerOrderPaymentResponse PaymentTrainerOrderResponse(int orderID);
}
