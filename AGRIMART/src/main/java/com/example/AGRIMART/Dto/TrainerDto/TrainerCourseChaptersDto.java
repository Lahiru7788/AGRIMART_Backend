package com.example.AGRIMART.Dto.TrainerDto;

import com.example.AGRIMART.Dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerCourseChaptersDto {

    private int chapterID;
    private String chapterName;
    private int chapterNo;
    private String chapterDescription;
    private boolean isActive;
    private UserDto user;
}
