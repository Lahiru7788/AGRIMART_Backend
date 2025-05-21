package com.example.AGRIMART.Service.SFService;

import com.example.AGRIMART.Dto.SFDto.SFOfferDto;
import com.example.AGRIMART.Dto.response.SFResponse.*;

public interface SFOfferService {
    SFOfferAddResponse saveOrUpdate(SFOfferDto sfOfferDto);
    SFOfferGetResponse getSFOffersByProductId(int productID);
    SFOfferDeleteResponse DeleteSFResponse(int productID);
}
