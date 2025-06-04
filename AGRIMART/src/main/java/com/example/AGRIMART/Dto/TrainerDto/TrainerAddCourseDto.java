package com.example.AGRIMART.Dto.TrainerDto;

import com.example.AGRIMART.Dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerAddCourseDto {

    private int courseID;
    private String courseName;
    private BigDecimal price;
    private String driveLink;
    private String youtubeLink;
    private String description;
    private Date addedDate;
    private boolean isDeleted;
    private boolean isAddedToCart;
    private boolean isRemovedFromCart;
    private String courseCategory;
    private UserDto user;
    public enum CourseCategory {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer
    }
}
