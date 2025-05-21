package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageGetResponse;

public interface ConsumerOrderImageService {
    ConsumerOrderImageAddResponse save(ConsumerOrderImageDto consumerOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    ConsumerOrderImageGetResponse GetConsumerOrderImageFindById(int orderID);
}
