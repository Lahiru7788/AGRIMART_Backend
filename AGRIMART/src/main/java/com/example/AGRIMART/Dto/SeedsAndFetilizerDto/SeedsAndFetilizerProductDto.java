package com.example.AGRIMART.Dto.SeedsAndFetilizerDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeedsAndFetilizerProductDto {

    private int productID;
    private String productName;
    private byte[] productImage;
    private BigDecimal price;
    private double availableQuantity;
    private double minimumQuantity;
    private String description;
    private Date addedDate;
    private boolean isActive;
    private boolean isDeleted;
    private boolean isQuantityLowered;
    private String productCategory;
    private UserDto user;
    public enum ProductCategory {
        Seeds,
        Fertilizer
    }
}
