package com.example.AGRIMART.Dto.FarmerDto;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmerConfirmSupermarketOrderDto {

    private int orderID;
    private int confirmOrderID;
    private String productName;
    private BigDecimal price;
    private double requiredQuantity;
    private String requiredTime ;
    private String description;
    private Date addedDate;
    private boolean isActive;
    private boolean isPaid;
    private UserDto user;
    private SupermarketAddOrderDto supermarketAddOrder;

}
