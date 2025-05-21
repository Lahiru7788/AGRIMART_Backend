package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.UserProfileGetResponse;

public interface CAddOrderImageService {
    CAddOrderImageAddResponse save(CAddOrderImageDto cAddOrderImageDto);
//    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
CAddOrderImageGetResponse GetCAddOrderImageFindById(int orderID);
}
