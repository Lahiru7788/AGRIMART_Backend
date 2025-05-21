package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;

public interface FarmerProductService {
    FarmerProductAddResponse saveOrUpdate(FarmerProductDto farmerProductDto);
    FarmerProductGetResponse getFarmerProductByUserId(int userID);
    FarmerProductDeleteResponse DeleteFarmerResponse(int productID);
    FarmerProductGetResponse GetAllFarmerProducts();
}
