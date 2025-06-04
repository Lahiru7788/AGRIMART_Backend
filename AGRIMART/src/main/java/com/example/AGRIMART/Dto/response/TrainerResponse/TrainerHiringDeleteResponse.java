package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerHiringDeleteResponse extends Response {
    private TrainerHiring trainerHiringDeleteResponse;
}

