package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainerCourseImageGetResponse extends Response {
    private List<TrainerCourseImageDto> TrainerCourseImageGetResponse;
}
