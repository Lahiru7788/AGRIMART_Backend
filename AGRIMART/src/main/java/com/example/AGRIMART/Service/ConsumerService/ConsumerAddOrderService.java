package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderConfirmResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;

public interface ConsumerAddOrderService {
    ConsumerAddOrderAddResponse saveOrUpdate(ConsumerAddOrderDto consumerAddOrderDto);
    ConsumerAddOrderGetResponse GetAllConsumerOrders();
    ConsumerAddOrderGetResponse getConsumerAddOrderByUserId(int userID);
    ConsumerAddOrderDeleteResponse DeleteConsumerResponse(int orderID);
    ConsumerAddOrderConfirmResponse ConfirmConsumerAddOrderResponse(int orderID);
}
