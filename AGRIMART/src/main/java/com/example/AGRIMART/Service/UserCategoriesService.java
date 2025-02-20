package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Dto.response.UserCategeriesAddResponse;
import com.example.AGRIMART.Dto.response.UserCategoriesGetResponse;



public interface UserCategoriesService {
    UserCategeriesAddResponse save(UserCategoriesDto userCategoriesDto);
    UserCategoriesGetResponse GetAllUserCategories();
}
