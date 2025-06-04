package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseChaptersDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersGetResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerOfferService;
import com.example.AGRIMART.Service.TrainerService.TrainerCourseChaptersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class TrainerCourseChaptersController {

    @Autowired
    private TrainerCourseChaptersService trainerCourseChaptersService;

    @PostMapping(path = "/trainerCourseChapters")
    public TrainerCourseChaptersAddResponse save(@RequestBody TrainerCourseChaptersDto trainerCourseChaptersDto) {
        return trainerCourseChaptersService.saveOrUpdate(trainerCourseChaptersDto);
    }

    @GetMapping("/viewTrainerCourseChaptersByCourseID/{courseID}")
    public TrainerCourseChaptersGetResponse findByTrainerCourse_CourseID(@PathVariable("courseID") int courseID) {
        return trainerCourseChaptersService.getTrainerCourseChaptersByCourseID(courseID);
    }

    @PutMapping(value = "/trainer-course-chapters/{chapterID}/delete")
    public TrainerCourseChaptersDeleteResponse DeleteTrainerCourseChaptersResponse(@PathVariable int chapterID){
        return trainerCourseChaptersService.DeleteTrainerCourseChaptersResponse(chapterID);

    }

}
