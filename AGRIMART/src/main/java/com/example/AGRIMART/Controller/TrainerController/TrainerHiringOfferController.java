package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringOfferDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Service.TrainerService.TrainerHiringOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class TrainerHiringOfferController {

    @Autowired
    private TrainerHiringOfferService trainerHiringOfferService;

    @PostMapping(path = "/trainerHiringOffers")
    public TrainerHiringOfferAddResponse save(@RequestBody TrainerHiringOfferDto trainerHiringOfferDto) {
        return trainerHiringOfferService.saveOrUpdate(trainerHiringOfferDto);
    }

    @GetMapping("/viewTrainerHiringOffersByCourseId/{courseID}")
    public TrainerHiringOfferGetResponse findByTrainerHiring_HireID(@PathVariable("hireID") int hireID) {
        return trainerHiringOfferService.getTrainerHiringOffersByHireId(hireID);
    }

    @PutMapping(value = "/trainer-hiring-offer/{offerID}/delete")
    public TrainerHiringOfferDeleteResponse DeleteTrainerHiringOfferResponse(@PathVariable int offerID){
        return trainerHiringOfferService.DeleteTrainerHiringOfferResponse(offerID);

    }
}
