package com.example.AGRIMART.Service.ConsumerService;

import com.example.AGRIMART.Dto.ConsumerDto.CPreOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CPreOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CPreOrderImageGetResponse;

public interface CPreOrderImageService {
    CPreOrderImageAddResponse save(CPreOrderImageDto cPreOrderImageDto);
    CPreOrderImageGetResponse GetAllConsumerPreOrderImages();
}
