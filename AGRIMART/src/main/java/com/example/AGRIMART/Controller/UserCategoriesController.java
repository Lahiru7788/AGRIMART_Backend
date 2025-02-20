package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Dto.response.UserCategeriesAddResponse;
import com.example.AGRIMART.Dto.response.UserCategoriesGetResponse;
import com.example.AGRIMART.Service.UserCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class UserCategoriesController {

   @Autowired
    private UserCategoriesService userCategoriesService;

    @PostMapping(path = "/userCategories")
    public UserCategeriesAddResponse save(@RequestBody UserCategoriesDto userCategoriesDto){
        return userCategoriesService.save(userCategoriesDto);
    }

    @GetMapping("/viewUserCategories")

    public UserCategoriesGetResponse getAllUserCategories() {
        return userCategoriesService.GetAllUserCategories();

    }
}
