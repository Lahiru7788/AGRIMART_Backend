package com.example.AGRIMART.Service.SeedsAndFertilizerService;

import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SeedsAndFetilizerProductDto;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SeedsAndFertilizerProductAddResponse;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SeedsAndFertilizerProductGetResponse;

public interface SeedsAndFertilizerProductService {
    SeedsAndFertilizerProductAddResponse saveOrUpdate(SeedsAndFetilizerProductDto seedsAndFetilizerProductDto);
    SeedsAndFertilizerProductGetResponse GetAllSeedsAndFertilizerProducts();
}
