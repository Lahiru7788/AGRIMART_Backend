package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseOfferDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrainerCourseOfferGetResponse extends Response {
    private List<TrainerCourseOfferDto> trainerCourseOfferGetResponse;

}