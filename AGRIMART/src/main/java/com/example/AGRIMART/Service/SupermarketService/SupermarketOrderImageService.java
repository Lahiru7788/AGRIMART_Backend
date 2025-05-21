package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderImageDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderImageGetResponse;

public interface SupermarketOrderImageService {

    SupermarketOrderImageAddResponse save(SupermarketOrderImageDto supermarketOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    SupermarketOrderImageGetResponse GetSupermarketOrderImageFindById(int orderID);
}
