package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringOfferDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerHiringOfferAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerHiringOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerHiringOfferGetResponse;

public interface TrainerHiringOfferService {
    TrainerHiringOfferAddResponse saveOrUpdate( TrainerHiringOfferDto trainerHiringOfferDto);
    TrainerHiringOfferGetResponse  getTrainerHiringOffersByHireId(int hiringID);
    TrainerHiringOfferDeleteResponse  DeleteTrainerHiringOfferResponse(int productID);
}
