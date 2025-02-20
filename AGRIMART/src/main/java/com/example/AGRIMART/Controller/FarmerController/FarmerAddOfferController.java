package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class FarmerAddOfferController {

    @Autowired
    private FarmerOfferService farmerOfferService;

    @PostMapping(path = "/farmerOffers")
    public FarmerOfferAddResponse save(@RequestBody FarmerOfferDto farmerOfferDto){
        return farmerOfferService.saveOrUpdate(farmerOfferDto);
    }

    @GetMapping("/viewFarmerOffers")

    public FarmerOfferGetResponse getAllFarmerOffers() {
        return farmerOfferService.GetAllFarmerOffers();

    }
}
