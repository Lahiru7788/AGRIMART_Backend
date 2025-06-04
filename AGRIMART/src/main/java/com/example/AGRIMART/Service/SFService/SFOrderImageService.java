package com.example.AGRIMART.Service.SFService;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOrderImageGetResponse;

public interface SFOrderImageService {

    SFOrderImageAddResponse save(SFOrderImageDto sfOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    SFOrderImageGetResponse GetSFOrderImageFindById(int orderID);
}
