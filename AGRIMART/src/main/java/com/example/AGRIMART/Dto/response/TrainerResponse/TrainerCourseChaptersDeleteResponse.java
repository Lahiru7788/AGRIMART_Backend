package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseChapters;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainerCourseChaptersDeleteResponse extends Response {
    private TrainerCourseChapters trainerCourseChaptersDeleteResponse;
}
