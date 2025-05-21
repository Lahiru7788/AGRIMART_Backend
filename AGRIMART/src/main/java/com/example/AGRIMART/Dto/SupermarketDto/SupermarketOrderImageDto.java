package com.example.AGRIMART.Dto.SupermarketDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupermarketOrderImageDto {

    private int imageID;
    private byte[] productImage;
    private UserDto user;
}
