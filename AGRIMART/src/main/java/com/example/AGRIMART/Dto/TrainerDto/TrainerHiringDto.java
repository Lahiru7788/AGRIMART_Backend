package com.example.AGRIMART.Dto.TrainerDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerHiringDto {

    private int hireID;
    private String name;
    private BigDecimal price;
    private int yearsOfExperience;
    private Date addedDate;
    private boolean isDeleted;
    private String qualifications;
    private UserDto user;

}
