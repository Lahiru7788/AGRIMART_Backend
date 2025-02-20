package com.example.AGRIMART.Dto.ConsumerDto;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
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
public class ConsumerAddOrderDto {

    private int orderID;
    private String productName;
    private BigDecimal price;
    private double requiredQuantity;
    private String requiredTime ;
    private String description;
    private Date addedDate;
    private boolean isActive;
    private boolean isConfirmed;
    private String productCategory;
    private UserDto user;
    public enum ProductCategory {
        Vegetables,
        Fruits,
        Cereals
    }
}
