package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;

public interface TrainerHiringService {

    TrainerHiringAddResponse saveOrUpdate(TrainerHiringDto trainerHiringDto);
    TrainerHiringGetResponse GetAllTrainerHirings();
    TrainerHiringGetResponse getTrainerHiringByUserID(int userID);
    TrainerHiringDeleteResponse DeleteTrainerHiringResponse(int orderID);
}
