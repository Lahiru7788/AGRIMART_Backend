package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;

public interface FarmerOfferService {
    FarmerOfferAddResponse saveOrUpdate(FarmerOfferDto farmerOfferDto);
    FarmerOfferGetResponse getFarmerOffersByProductId(int productID);
    FarmerOfferDeleteResponse DeleteFarmerResponse(int productID);

}
