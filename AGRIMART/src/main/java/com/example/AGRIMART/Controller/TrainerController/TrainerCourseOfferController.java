package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseOfferDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferGetResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerOfferService;
import com.example.AGRIMART.Service.TrainerService.TrainerCourseOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class TrainerCourseOfferController {

    @Autowired
    private TrainerCourseOfferService trainerCourseOfferService;

    @PostMapping(path = "/trainerCourseOffers")
    public TrainerCourseOfferAddResponse save(@RequestBody TrainerCourseOfferDto trainerCourseOfferDto) {
        return trainerCourseOfferService.saveOrUpdate(trainerCourseOfferDto);
    }

    @GetMapping("/viewTrainerCourseOffersByProductId/{courseID}")
    public TrainerCourseOfferGetResponse findByTrainerCourse_CourseID(@PathVariable("courseID") int courseID) {
        return trainerCourseOfferService.getTrainerCourseOffersByCourseId(courseID);
    }

    @PutMapping(value = "/trainer-course-offer/{offerID}/delete")
    public TrainerCourseOfferDeleteResponse DeleteTrainerCourseOfferResponse(@PathVariable int offerID){
        return trainerCourseOfferService.DeleteTrainerCourseOfferResponse(offerID);

    }
}
