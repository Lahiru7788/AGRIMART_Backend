package com.example.AGRIMART.Dto.TrainerDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerCourseImageDto {

    private int imageID;
    private byte[] courseImage;
    private UserDto user;
}
