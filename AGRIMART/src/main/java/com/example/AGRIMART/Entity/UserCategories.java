package com.example.AGRIMART.Entity;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userCategories")
public class UserCategories {

    @Id
    @Column(name = "usercategories_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userCategoriesID;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "category_one")
    @Enumerated(EnumType.STRING)
    private UserCategoriesDto.CategoryOne categoryOne;

    @Column(name = "category_two")
    @Enumerated(EnumType.STRING)
    private UserCategoriesDto.CategoryTwo categoryTwo;

    @Column(name = "category_three")
    @Enumerated(EnumType.STRING)
    private UserCategoriesDto.CategoryThree categoryThree;

    @Column(name = "category_four")
    @Enumerated(EnumType.STRING)
    private UserCategoriesDto.CategoryFour categoryFour;

    @Column(name = "category_five")
    @Enumerated(EnumType.STRING)
    private UserCategoriesDto.CategoryFive categoryFive;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;


}
