package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketSeedsOrderImageGetResponse;

public interface SupermarketSeedsOrderImageService {
    SupermarketSeedsOrderImageAddResponse save(SupermarketSeedsOrderImageDto supermarketSeedsOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    SupermarketSeedsOrderImageGetResponse GetSupermarketSeedsOrderImageFindById(int orderID);
}
