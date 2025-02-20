package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;

public interface FarmerProductService {
    FarmerProductAddResponse saveOrUpdate(FarmerProductDto farmerProductDto);
    FarmerProductGetResponse GetAllFarmerProducts();
}
