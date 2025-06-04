package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;

public interface ConsumerCourseOrderService {

    ConsumerCourseOrderAddResponse saveOrUpdate(ConsumerCourseOrderDto consumerCourseOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerCourseOrderByProductId(int productID);
    ConsumerCourseOrderGetResponse getConsumerCourseOrderByUserId(int userID);
    ConsumerCourseOrderDeleteResponse DeleteConsumerCourseOrderResponse(int orderID);
    ConsumerCourseOrderGetResponse getConsumerCourseOrdersByCourseId(int courseID);
    ConsumerCourseOrderGetResponse getConsumerCourseOrderByTrainerUserId(int userID);
    ConsumerCourseOrderAddToCartResponse AddToCartConsumerCourseOrderResponse(int orderID);
    ConsumerCourseOrderRemovedFromCartResponse RemovedFromCartConsumerCourseOrderResponse(int orderID);
    ConsumerCourseOrderPaymentResponse PaymentConsumerCourseOrderResponse(int orderID);
}
