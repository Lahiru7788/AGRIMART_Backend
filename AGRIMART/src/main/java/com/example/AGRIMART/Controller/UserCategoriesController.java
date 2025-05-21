package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Dto.response.UserCategeriesAddResponse;
import com.example.AGRIMART.Dto.response.UserCategoriesGetResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Service.UserCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class UserCategoriesController {

   @Autowired
    private UserCategoriesService userCategoriesService;

    @PostMapping(path = "/userCategories")
    public UserCategeriesAddResponse save(@RequestBody UserCategoriesDto userCategoriesDto){
        return userCategoriesService.save(userCategoriesDto);
    }

    @GetMapping("/viewUserCategories/{userID}")

    public UserCategoriesGetResponse findByUser_UserID(@PathVariable("userID") int userID)  {
        return userCategoriesService.getUserCategoriesByUserID(userID);

    }
}
