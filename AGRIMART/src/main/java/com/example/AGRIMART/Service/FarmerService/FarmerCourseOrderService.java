package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerCourseOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface FarmerCourseOrderService {

    FarmerCourseOrderAddResponse saveOrUpdate(FarmerCourseOrderDto farmerCourseOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getFarmerCourseOrderByProductId(int productID);
    FarmerCourseOrderGetResponse getFarmerCourseOrderByUserId(int userID);
    FarmerCourseOrderDeleteResponse DeleteFarmerCourseOrderResponse(int orderID);
    FarmerCourseOrderGetResponse getFarmerCourseOrdersByCourseId(int courseID);
    FarmerCourseOrderGetResponse getFarmerCourseOrderByTrainerUserId(int userID);
    FarmerCourseOrderAddToCartResponse AddToCartFarmerCourseOrderResponse(int orderID);
    FarmerCourseOrderRemovedFromCartResponse RemovedFromCartFarmerCourseOrderResponse(int orderID);
    FarmerCourseOrderPaymentResponse PaymentFarmerCourseOrderResponse(int orderID);
}
