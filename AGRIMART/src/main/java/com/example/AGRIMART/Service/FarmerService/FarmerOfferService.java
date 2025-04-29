package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;

import java.util.List;

public interface FarmerOfferService {
    FarmerOfferAddResponse saveOrUpdate(FarmerOfferDto farmerOfferDto);
    FarmerOfferGetResponse getFarmerOffersByProductId(int productID);
    FarmerOfferDeleteResponse DeleteFarmerResponse(int productID);

}
