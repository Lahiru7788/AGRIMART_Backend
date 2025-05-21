package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SAddOrderImageDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageGetResponse;

public interface SAddOrderImageService {
    SAddOrderImageAddResponse save(SAddOrderImageDto sAddOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    SAddOrderImageGetResponse GetSAddOrderImageFindById(int orderID);
}
