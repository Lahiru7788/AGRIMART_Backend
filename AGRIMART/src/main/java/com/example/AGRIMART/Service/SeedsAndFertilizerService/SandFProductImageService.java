package com.example.AGRIMART.Service.SeedsAndFertilizerService;

import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SandFProductImageDto;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SandFProductImageAddResponse;

public interface SandFProductImageService {
    SandFProductImageAddResponse save(SandFProductImageDto sandFProductImageDto);
}
