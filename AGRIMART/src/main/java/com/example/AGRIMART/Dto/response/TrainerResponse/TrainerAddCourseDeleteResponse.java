package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerAddCourseDeleteResponse extends Response {
    private TrainerCourse trainerAddCourseDeleteResponse;
}

