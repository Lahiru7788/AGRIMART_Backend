package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringDto;
import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Service.TrainerService.TrainerHiringService;
import com.example.AGRIMART.Service.UserDetailsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class TrainerHiringController {

    @Autowired
    private TrainerHiringService trainerHiringService;

//    @PostMapping(path = "/trainerHiring")
//    public TrainerHiringAddResponse save(@RequestBody TrainerHiringDto trainerHiringDto, HttpSession session){
//        TrainerHiringAddResponse response = trainerHiringService.saveOrUpdate(trainerHiringDto);
//
//        if ("200".equals(response.getStatus())) {
//            session.setAttribute("name", trainerHiringDto.getName());
//        }
//
//        return response;
//    }

    @PostMapping(path = "/trainerHiring")
    public TrainerHiringAddResponse save(@Valid @RequestBody TrainerHiringDto trainerHiringDto, HttpSession session) {
        System.out.println("Received TrainerHiringDto: " + trainerHiringDto);
        TrainerHiringAddResponse response = trainerHiringService.saveOrUpdate(trainerHiringDto);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Hiring ID from DTO: " + trainerHiringDto.getHireID());
        System.out.println("Hiring ID from Response: " + response.getHireID());
        if ("200".equals(response.getStatus()) && response.getHireID() != null) {
            int hireID = response.getHireID(); // Use auto-generated ID from response
            session.setAttribute("hireID", hireID);
            System.out.println("Hiring ID set in session: " + hireID);
        } else {
            System.out.println("Failed to set Hiring ID in session due to status: " + response.getStatus());
        }
        System.out.println("Session hireID: " + session.getAttribute("hireID"));
        return response;
    }

    @GetMapping("/viewAllHires")

    public TrainerHiringGetResponse GetAllTrainerHirings() {
        return trainerHiringService.GetAllTrainerHirings();

    }

    @GetMapping("/viewTrainerHiring/{userID}")
    public TrainerHiringGetResponse findByUser_UserID(@PathVariable("userID") int userID)  {
        return trainerHiringService.getTrainerHiringByUserID(userID);

    }

    @PutMapping(value = "/trainer-hiring/{hireID}/delete")
    public TrainerHiringDeleteResponse DeleteTrainerHiringResponse(@PathVariable int hireID){
        return trainerHiringService.DeleteTrainerHiringResponse(hireID);

    }
}
