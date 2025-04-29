package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class FarmerAddOfferController {

    @Autowired
    private FarmerOfferService farmerOfferService;

    @PostMapping(path = "/farmerOffers")
    public FarmerOfferAddResponse save(@RequestBody FarmerOfferDto farmerOfferDto) {
        return farmerOfferService.saveOrUpdate(farmerOfferDto);
    }

    @GetMapping("/viewFarmerOffersByProductId/{productID}")
    public FarmerOfferGetResponse findByFarmerProduct_ProductID(@PathVariable("productID") int productID) {
        return farmerOfferService.getFarmerOffersByProductId(productID);
    }

    @PutMapping(value = "/farmer-product-offer/{offerID}/delete")
    public FarmerOfferDeleteResponse DeleteFarmerResponse(@PathVariable int offerID){
        return farmerOfferService.DeleteFarmerResponse(offerID);

    }

}