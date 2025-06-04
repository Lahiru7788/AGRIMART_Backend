package com.example.AGRIMART.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBilingDetailsDto {


    private int userBillingDetailsID;
    private String address;
    private String userFirstName;
    private String userLastName;
    private Date addedDate;
    private String postalCode;
    private String mobile;
    private String country;
    private UserDto user;
}
