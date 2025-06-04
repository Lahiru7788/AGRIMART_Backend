package com.example.AGRIMART.Dto.SFDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SFOrderImageDto {

    private int imageID;
    private byte[] productImage;
    private UserDto user;
}
