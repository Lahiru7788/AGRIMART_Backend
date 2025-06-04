package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerHireDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import com.example.AGRIMART.Service.FarmerService.FarmerHireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class FarmerHireController {

    @Autowired
    private FarmerHireService farmerHireService;

    @PostMapping(path = "/farmerHire")
    public FarmerHireAddResponse save(@RequestBody FarmerHireDto farmerHireDto) {
        return farmerHireService.saveOrUpdate(farmerHireDto);
    }

    @GetMapping("/viewFarmerHire/{hireID}")
    public FarmerHireGetResponse findByTrainerHire_HireID(@PathVariable("hireID") int hireID) {
        return farmerHireService.getFarmerHireByHireId(hireID);
    }

    @GetMapping("/viewFarmerHireByFarmerID/{userID}")  // Changed the path to be more specific
    public FarmerHireGetResponse findByUserID(@PathVariable("userID") int userID) {
        return farmerHireService.getFarmerHireByUserId(userID);
    }

    @GetMapping("/viewFarmerHireByTrainerID/{trainerID}")
    public FarmerHireGetResponse findByTrainerID(@PathVariable("trainerID") int trainerID) {
        return farmerHireService.getFarmerHireByTrainerUserId(trainerID);
    }

    @PutMapping(value = "/farmer-hire/{orderID}/delete")
    public FarmerHireDeleteResponse DeleteFarmerHireResponse(@PathVariable int orderID){
        return farmerHireService.DeleteFarmerHireResponse(orderID);

    }

    @PutMapping(value = "/farmer-hire/{orderID}/confirm")
    public FarmerHireConfirmResponse ConfirmFarmerHireResponse(@PathVariable int orderID){
        return farmerHireService.ConfirmFarmerHireResponse(orderID);

    }

    @PutMapping(value = "/farmer-hire/{orderID}/reject")
    public FarmerHireRejectResponse RejectFarmerHireResponse(@PathVariable int orderID){
        return farmerHireService.RejectFarmerHireResponse(orderID);

    }

    @PutMapping(value = "/farmer-hire-addedToCart/{orderID}/confirm")
    public FarmerHireAddToCartResponse AddToCartFarmerHireResponse(@PathVariable int orderID){
        return farmerHireService.AddToCartFarmerHireResponse(orderID);

    }

    @PutMapping(value = "/farmer-hire-removedFromCart/{orderID}/confirm")
    public FarmerHireRemovedFromCartResponse RemovedFromCartFarmerHireResponse(@PathVariable int orderID){
        return farmerHireService.RemovedFromCartFarmerHireResponse(orderID);

    }

    @PutMapping(value = "/farmer-hire-payment/{orderID}/confirm")
    public FarmerHirePaymentResponse PaymentFarmerHireResponse(@PathVariable int orderID){
        return farmerHireService.PaymentFarmerHireResponse(orderID);

    }
}
