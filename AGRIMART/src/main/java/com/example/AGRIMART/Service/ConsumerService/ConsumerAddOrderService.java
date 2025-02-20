package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;

public interface ConsumerAddOrderService {
    ConsumerAddOrderAddResponse saveOrUpdate(ConsumerAddOrderDto consumerAddOrderDto);
    ConsumerAddOrderGetResponse GetAllConsumerOrders();
}
