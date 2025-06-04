package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainerAddCourseGetResponse extends Response {
    private List<TrainerAddCourseDto> trainerAddCourseGetResponse;
}