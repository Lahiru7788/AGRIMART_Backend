package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerPreOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerPreOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerPreOrderGetResponse;

public interface ConsumerPreOrderService {
    ConsumerPreOrderAddResponse saveOrUpdate(ConsumerPreOrderDto consumerPreOrderDto);
    ConsumerPreOrderGetResponse GetAllConsumerPreOrders();
}
