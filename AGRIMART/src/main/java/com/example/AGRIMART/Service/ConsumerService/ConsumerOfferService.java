package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;

public interface ConsumerOfferService {
    ConsumerOfferAddResponse saveOrUpdate(ConsumerOfferDto consumerOfferDto);
//    ConsumerOfferGetResponse GetAllConsumerOffers();
    ConsumerOfferGetResponse getConsumerAddOffersByOrderId(int orderID);
}
