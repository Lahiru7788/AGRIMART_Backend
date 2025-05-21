package com.example.AGRIMART.Controller.SFController;

import com.example.AGRIMART.Dto.SFDto.SFOfferDto;
import com.example.AGRIMART.Dto.response.SFResponse.SFOfferAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOfferGetResponse;
import com.example.AGRIMART.Service.SFService.SFOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class SFAddOfferController {

    @Autowired
    private SFOfferService sfOfferService;

    @PostMapping(path = "/sAndFProductOffers")
    public SFOfferAddResponse save(@RequestBody SFOfferDto sfOfferDto) {
        return sfOfferService.saveOrUpdate(sfOfferDto);
    }

    @GetMapping("/viewsAndFProductOffersByProductId/{productID}")
    public SFOfferGetResponse findBySFProduct_ProductID(@PathVariable("productID") int productID) {
        return sfOfferService.getSFOffersByProductId(productID);
    }

    @PutMapping(value = "/sf-product-offer/{offerID}/delete")
    public SFOfferDeleteResponse DeleteFarmerResponse(@PathVariable int offerID){
        return sfOfferService.DeleteSFResponse(offerID);

    }
}
