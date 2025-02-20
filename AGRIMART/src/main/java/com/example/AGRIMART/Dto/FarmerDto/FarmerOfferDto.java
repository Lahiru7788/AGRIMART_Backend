package com.example.AGRIMART.Dto.FarmerDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmerOfferDto {

    private int offerID;
    private String offerName;
    private BigDecimal newPrice;
    private String offerDescription;
    private boolean isActive;
    private UserDto user;
}
