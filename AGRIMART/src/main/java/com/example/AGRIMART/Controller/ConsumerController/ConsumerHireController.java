package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerHireDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerHireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class ConsumerHireController {

    @Autowired
    private ConsumerHireService consumerHireService;

    @PostMapping(path = "/consumerHire")
    public ConsumerHireAddResponse save(@RequestBody ConsumerHireDto consumerHireDto) {
        return consumerHireService.saveOrUpdate(consumerHireDto);
    }

    @GetMapping("/viewConsumerHire/{hireID}")
    public ConsumerHireGetResponse findByTrainerHire_HireID(@PathVariable("hireID") int hireID) {
        return consumerHireService.getConsumerHireByHireId(hireID);
    }

    @GetMapping("/viewConsumerHireByConsumerID/{userID}")  // Changed the path to be more specific
    public ConsumerHireGetResponse findByUserID(@PathVariable("userID") int userID) {
        return consumerHireService.getConsumerHireByUserId(userID);
    }

    @GetMapping("/viewConsumerHireByTrainerID/{trainerID}")
    public ConsumerHireGetResponse findByTrainerID(@PathVariable("trainerID") int trainerID) {
        return consumerHireService.getConsumerHireByTrainerUserId(trainerID);
    }

    @PutMapping(value = "/consumer-hire/{orderID}/delete")
    public ConsumerHireDeleteResponse DeleteConsumerHireResponse(@PathVariable int orderID){
        return consumerHireService.DeleteConsumerHireResponse(orderID);

    }

    @PutMapping(value = "/consumer-hire/{orderID}/confirm")
    public ConsumerHireConfirmResponse ConfirmConsumerHireResponse(@PathVariable int orderID){
        return consumerHireService.ConfirmConsumerHireResponse(orderID);

    }

    @PutMapping(value = "/consumer-hire/{orderID}/reject")
    public ConsumerHireRejectResponse RejectConsumerHireResponse(@PathVariable int orderID){
        return consumerHireService.RejectConsumerHireResponse(orderID);

    }

    @PutMapping(value = "/consumer-hire-addedToCart/{orderID}/confirm")
    public ConsumerHireAddToCartResponse AddToCartConsumerHireResponse(@PathVariable int orderID){
        return consumerHireService.AddToCartConsumerHireResponse(orderID);

    }

    @PutMapping(value = "/consumer-hire-removedFromCart/{orderID}/confirm")
    public ConsumerHireRemovedFromCartResponse RemovedFromCartConsumerHireResponse(@PathVariable int orderID){
        return consumerHireService.RemovedFromCartConsumerHireResponse(orderID);

    }

    @PutMapping(value = "/consumer-hire-payment/{orderID}/confirm")
    public ConsumerHirePaymentResponse PaymentConsumerHireResponse(@PathVariable int orderID){
        return consumerHireService.PaymentConsumerHireResponse(orderID);

    }
}
