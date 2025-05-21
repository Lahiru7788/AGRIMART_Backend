package com.example.AGRIMART.Dto.ConsumerDto;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerOrderDto {
    private int orderID;
//    private int productID;
    private String productName;
    private BigDecimal price;
    private double requiredQuantity;
    private String description;
    private Date addedDate;
    private boolean isActive;
    private boolean isConfirmed;
    private boolean isRejected;
    private boolean isAddedToCart;
    private boolean isRemovedFromCart;
    private String productCategory;
    private UserDto user;
    private FarmerProductDto farmerProduct;
    private SFProductDto sfProduct;
    public enum ProductCategory {
        Vegetables,
        Fruits,
        Cereals
    }
}
