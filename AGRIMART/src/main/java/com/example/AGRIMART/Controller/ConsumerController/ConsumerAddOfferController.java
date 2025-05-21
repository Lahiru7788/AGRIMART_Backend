package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOfferService;
import com.example.AGRIMART.Service.FarmerService.FarmerOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class ConsumerAddOfferController {

    @Autowired
    private ConsumerOfferService consumerOfferService;

    @PostMapping(path = "/consumerOffers")
    public ConsumerOfferAddResponse save(@RequestBody ConsumerOfferDto consumerOfferDto){
        return consumerOfferService.saveOrUpdate(consumerOfferDto);
    }
//
//    @GetMapping("/viewConsumerOffers")
//
//    public ConsumerOfferGetResponse getAllConsumerOffers() {
//        return consumerOfferService.GetAllConsumerOffers();
//
//    }

    @GetMapping("/viewConsumerOffers/{orderID}")
    public ConsumerOfferGetResponse findByConsumerAddOrder_OrderID(@PathVariable("orderID") int orderID) {
        return consumerOfferService.getConsumerAddOffersByOrderId(orderID);
    }

//    @PutMapping(value = "/farmer-product-offer/{offerID}/delete")
//    public FarmerOfferDeleteResponse DeleteFarmerResponse(@PathVariable int offerID){
//        return farmerOfferService.DeleteFarmerResponse(offerID);
//
//    }
}
