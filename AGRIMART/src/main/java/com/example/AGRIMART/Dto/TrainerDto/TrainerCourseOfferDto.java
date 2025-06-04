package com.example.AGRIMART.Dto.TrainerDto;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerCourseOfferDto {

    private int offerID;
    private int productID;
    private String offerName;
    private BigDecimal newPrice;
    private String offerDescription;
    private boolean isActive;
    private TrainerAddCourseDto TrainerCourse;
    private UserDto user;
}
