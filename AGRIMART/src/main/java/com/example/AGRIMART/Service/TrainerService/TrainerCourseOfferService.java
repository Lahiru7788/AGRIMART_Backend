package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseOfferDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferGetResponse;

public interface TrainerCourseOfferService {
    TrainerCourseOfferAddResponse saveOrUpdate(TrainerCourseOfferDto trainerCourseOfferDto);
    TrainerCourseOfferGetResponse getTrainerCourseOffersByCourseId(int courseID);
    TrainerCourseOfferDeleteResponse DeleteTrainerCourseOfferResponse(int productID);
}
