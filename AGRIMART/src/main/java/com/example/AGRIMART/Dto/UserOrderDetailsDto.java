package com.example.AGRIMART.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderDetailsDto {

    private int userOrderDetailsID;
    private int orderNumber;
    private Date addedDate;
    private String userType;
    private BigDecimal price;
    private double quantity;
    private UserDto user;
}
