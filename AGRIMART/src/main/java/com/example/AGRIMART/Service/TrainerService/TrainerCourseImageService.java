package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseImageDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseImageAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseImageGetResponse;

public interface TrainerCourseImageService {

    TrainerCourseImageAddResponse save(TrainerCourseImageDto trainerCourseImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
    TrainerCourseImageGetResponse GetTrainerCourseImageFindById(int courseID);
}
