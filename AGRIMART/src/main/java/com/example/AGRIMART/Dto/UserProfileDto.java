package com.example.AGRIMART.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private int profileID;
    private byte[] profilePicture;
    private byte[] coverPicture;
    private UserDto user;

}
