package com.example.AGRIMART.Dto.FarmerDto;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringDto;
import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmerHireDto {

    private int orderID;
    private int hireID;
    private String name;
    private BigDecimal price;
    private int yearsOfExperience;
    private Date addedDate;
    private boolean isActive;
    private boolean isConfirmed;
    private boolean isRejected;
    private boolean isAddedToCart;
    private boolean isRemovedFromCart;
    private boolean isPaid;
    private String qualifications;
    private TrainerHiringDto trainerHiring;
    private UserDto user;
}
