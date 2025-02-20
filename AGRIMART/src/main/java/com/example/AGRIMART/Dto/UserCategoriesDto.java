package com.example.AGRIMART.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCategoriesDto {

    private int userCategoriesID;
    private String aboutMe;
    private String categoryOne;
    private String categoryTwo;
    private String categoryThree;
    private String categoryFour;
    private String categoryFive;
    private UserDto user;

    public enum CategoryOne {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer,

    }

    public enum CategoryTwo {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer,

    }

    public enum CategoryThree {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer,

    }

    public enum CategoryFour {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer,

    }

    public enum CategoryFive {
        Vegetables,
        Fruits,
        Cereals,
        Seeds,
        Fertilizer,

    }
}
