package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOfferDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOfferAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOfferGetResponse;

public interface SupermarketOfferService {
    SupermarketOfferAddResponse saveOrUpdate(SupermarketOfferDto supermarketOfferDto);
    //    ConsumerOfferGetResponse GetAllConsumerOffers();
    SupermarketOfferGetResponse getSupermarketAddOffersByOrderId(int orderID);
}
