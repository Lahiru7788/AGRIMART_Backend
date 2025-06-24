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
public class TrainerHiringOfferDto {
    private int offerID;
    private int hireID;
    private String offerName;
    private BigDecimal newPrice;
    private String offerDescription;
    private boolean isActive;
    private TrainerHiringDto trainerHiring;
    private UserDto user;
}
