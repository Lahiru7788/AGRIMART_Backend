package com.example.AGRIMART.Service.FarmerService;

import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderImageGetResponse;

public interface FarmerSeedsOrderImageService {

    FarmerSeedsOrderImageAddResponse save(FarmerSeedsOrderImageDto farmerSeedsOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    FarmerSeedsOrderImageGetResponse GetFarmerSeedsOrderImageFindById(int orderID);
}

