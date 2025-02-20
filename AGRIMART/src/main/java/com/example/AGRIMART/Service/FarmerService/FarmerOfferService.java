package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;

public interface FarmerOfferService {
    FarmerOfferAddResponse saveOrUpdate(FarmerOfferDto farmerOfferDto);
    FarmerOfferGetResponse GetAllFarmerOffers();
}
