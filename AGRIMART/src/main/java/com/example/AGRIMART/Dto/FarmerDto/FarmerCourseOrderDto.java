package com.example.AGRIMART.Dto.FarmerDto;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmerCourseOrderDto {

    private int orderID;
    private int courseID;
    private String courseName;
    private BigDecimal price;
    private String description;
    private Date addedDate;
    private boolean isActive;
    private boolean isAddedToCart;
    private boolean isRemovedFromCart;
    private boolean isPaid;
    private String courseCategory;
    private UserDto user;
    private TrainerAddCourseDto trainerCourse;
    public enum CourseCategory {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer
    }
}
