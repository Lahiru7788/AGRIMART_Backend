package com.example.AGRIMART.Service.SFService;

import com.example.AGRIMART.Dto.SFDto.SFProductImageDto;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductImageAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductImageGetResponse;

public interface SFProductImageService {
    SFProductImageAddResponse save(SFProductImageDto SFProductImageDto);
    SFProductImageGetResponse GetSAndFProductImageFindById(int productID);
}
