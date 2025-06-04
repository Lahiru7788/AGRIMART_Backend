package com.example.AGRIMART.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentDto {

    private int userPaymentID;
    private String userName;
    private long cardNumber;
    private int cvcNumber;
    private int expiryMonth;
    private int expiryYear;
    private UserDto user;

}
