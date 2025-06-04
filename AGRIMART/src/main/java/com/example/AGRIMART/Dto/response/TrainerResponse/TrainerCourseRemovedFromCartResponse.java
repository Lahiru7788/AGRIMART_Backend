package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerCourseRemovedFromCartResponse extends Response {
    private TrainerCourse trainerCourseRemovedFromCartResponse;

}
