package com.example.AGRIMART.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int userID;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String userType;

    public enum UserType {
        Farmer,
        SeedsAndFertilizerSeller,
        FarmerTrainer,
        Consumer,
        Supermarket
    }
}
