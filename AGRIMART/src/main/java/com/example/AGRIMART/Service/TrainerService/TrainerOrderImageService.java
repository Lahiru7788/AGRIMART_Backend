package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderImageDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerOrderImageGetResponse;

public interface TrainerOrderImageService {


    TrainerOrderImageAddResponse save(TrainerOrderImageDto trainerOrderImageDto);
    //    CAddOrderImageGetResponse GetAllConsumerAddOrderImages();
   TrainerOrderImageGetResponse GetTrainerOrderImageFindById(int orderID);
}
