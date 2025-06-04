package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseOffer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainerCourseOfferDeleteResponse extends Response {
    private TrainerCourseOffer trainerCourseOfferDeleteResponse;
}
