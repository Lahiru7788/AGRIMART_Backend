package com.example.AGRIMART.Service.SFService;

import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.response.SFResponse.SeedsAndFertilizerDeleteResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductGetResponse;

public interface SFProductService {
    SFProductAddResponse saveOrUpdate(SFProductDto SFProductDto);
    SFProductGetResponse getSeedsAndFertilizerProductByUserId(int userID);
    SeedsAndFertilizerDeleteResponse DeleteSeedsAndFertilizerResponse(int productID);
    SFProductGetResponse GetAllSeedsAndFertilizerProducts();
}
