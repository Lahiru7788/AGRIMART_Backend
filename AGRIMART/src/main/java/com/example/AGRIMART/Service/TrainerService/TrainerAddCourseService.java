package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketSeedsOrderAddToCartResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketSeedsOrderRemovedFromCartResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;

public interface TrainerAddCourseService {

    TrainerAddCourseAddResponse saveOrUpdate(TrainerAddCourseDto trainerAddCourseDto);
    TrainerAddCourseGetResponse GetAllTrainerCourses();
    TrainerAddCourseGetResponse getTrainerAddCourseByUserId(int userID);
    TrainerAddCourseDeleteResponse DeleteTrainerResponse(int orderID);
//    TrainerCourseAddToCartResponse AddToCartTrainerCourseResponse(int courseID);
//    TrainerCourseRemovedFromCartResponse RemovedFromCartTrainerCourseResponse(int courseID);
}
