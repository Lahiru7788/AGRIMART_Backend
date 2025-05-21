package com.example.AGRIMART.Dto.SupermarketDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupermarketAddOrderDto {

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
