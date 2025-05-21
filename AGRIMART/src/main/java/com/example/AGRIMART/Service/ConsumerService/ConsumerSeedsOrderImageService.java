package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageGetResponse;

public interface ConsumerSeedsOrderImageService {

    ConsumerSeedsOrderImageAddResponse save(ConsumerSeedsOrderImageDto consumerSeedsOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    ConsumerSeedsOrderImageGetResponse GetConsumerSeedsOrderImageFindById(int orderID);
}
