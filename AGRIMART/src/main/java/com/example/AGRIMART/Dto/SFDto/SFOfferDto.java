package com.example.AGRIMART.Dto.SFDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SFOfferDto {

    private int offerID;
    private int productID;
    private String offerName;
    private BigDecimal newPrice;
    private String offerDescription;
    private boolean isActive;
    private UserDto user;
}
