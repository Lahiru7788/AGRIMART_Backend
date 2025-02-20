package com.example.AGRIMART.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    private int userDetailsID;
    private String address;
    private String userFirstName;
    private String userLastName;
    private String city;
    private String postalCode;
    private String mobile;
    private String country;
    private UserDto user;
}
