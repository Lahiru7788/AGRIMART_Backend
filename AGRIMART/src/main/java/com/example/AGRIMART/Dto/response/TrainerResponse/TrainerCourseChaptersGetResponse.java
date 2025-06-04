package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseChaptersDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrainerCourseChaptersGetResponse extends Response {
    private List<TrainerCourseChaptersDto> trainerCourseChaptersGetResponse;
}
