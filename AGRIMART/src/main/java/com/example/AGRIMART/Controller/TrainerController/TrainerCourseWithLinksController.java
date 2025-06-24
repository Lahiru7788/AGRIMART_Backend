package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderAddToCartResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderRemovedFromCartResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Service.TrainerService.TrainerAddCourseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class TrainerCourseWithLinksController {

    @Autowired
    private TrainerAddCourseService trainerAddCourseService;

//    @PostMapping(path = "/trainerAddCourse")
//    public TrainerAddCourseAddResponse save(@RequestBody TrainerAddCourseDto trainerAddCourseDto, HttpSession session){
//        TrainerAddCourseAddResponse response = trainerAddCourseService.saveOrUpdate(trainerAddCourseDto);
//
//        if ("200".equals(response.getStatus())) {
//            session.setAttribute("courseName", trainerAddCourseDto.getCourseName());
//        }
//
//        return response;
//    }

    @PostMapping(path = "/trainerAddCourse")
    public TrainerAddCourseAddResponse save(@Valid @RequestBody TrainerAddCourseDto trainerAddCourseDto, HttpSession session) {
        System.out.println("Received TrainerAddCourseDto: " + trainerAddCourseDto);
        TrainerAddCourseAddResponse response = trainerAddCourseService.saveOrUpdate(trainerAddCourseDto);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Course ID from DTO: " + trainerAddCourseDto.getCourseID());
        System.out.println("Course ID from Response: " + response.getCourseID());
        if ("200".equals(response.getStatus()) && response.getCourseID() != null) {
            int courseID = response.getCourseID(); // Use auto-generated ID from response
            session.setAttribute("courseID", courseID);
            System.out.println("Course ID set in session: " + courseID);
        } else {
            System.out.println("Failed to set Course ID in session due to status: " + response.getStatus());
        }
        System.out.println("Session courseID: " + session.getAttribute("courseID"));
        return response;
    }

    @GetMapping("/viewCourses")

    public TrainerAddCourseGetResponse GetAllTrainerCourses() {
        return trainerAddCourseService.GetAllTrainerCourses();

    }

    @GetMapping("/viewCourses/{userID}")
    public TrainerAddCourseGetResponse findByUser_UserID(@PathVariable("userID") int userID) {
        return trainerAddCourseService.getTrainerAddCourseByUserId(userID);
    }

    @PutMapping(value = "/trainer-course/{courseID}/delete")
    public TrainerAddCourseDeleteResponse DeleteTrainerResponse(@PathVariable int courseID){
        return trainerAddCourseService.DeleteTrainerResponse(courseID);

    }

}
