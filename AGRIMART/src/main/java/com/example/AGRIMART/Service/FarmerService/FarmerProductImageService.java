package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;

public interface FarmerProductImageService {
    FProductImageAddResponse save(FarmerProductImageDto farmerProductImageDto);
//    FProductImageGetResponse GetAllFarmerProductImages();
    FProductImageGetResponse GetFarmerProductImageFindById(int productID);

}
