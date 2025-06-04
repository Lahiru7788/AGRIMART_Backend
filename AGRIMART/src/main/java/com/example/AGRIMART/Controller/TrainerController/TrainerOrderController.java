package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOrderService;
import com.example.AGRIMART.Service.TrainerService.TrainerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class TrainerOrderController {

    @Autowired
    private TrainerOrderService trainerOrderService;

    @PostMapping(path = "/trainerOrderProducts")
    public TrainerOrderAddResponse save(@RequestBody TrainerOrderDto trainerOrderDto) {
        return trainerOrderService.saveOrUpdate(trainerOrderDto);
    }

    @GetMapping("/viewTrainerOrders/{productID}")
    public TrainerOrderGetResponse findByFarmerProduct_ProductID(@PathVariable("productID") int productID) {
        return trainerOrderService.getTrainerOrdersByProductId(productID);
    }

    @GetMapping("/viewTrainerOrdersByTrainerID/{userID}")  // Changed the path to be more specific
    public TrainerOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return trainerOrderService.getTrainerOrderByUserId(userID);
    }

    @GetMapping("/viewTrainerOrdersByFarmerID/{farmerID}")
    public TrainerOrderGetResponse findByFarmerID(@PathVariable("farmerID") int farmerID) {
        return trainerOrderService.getTrainerOrderByFarmerUserId(farmerID);
    }

    @PutMapping(value = "/trainer-order-products/{orderID}/delete")
    public TrainerOrderDeleteResponse DeleteTrainerOrderResponse(@PathVariable int orderID){
        return trainerOrderService.DeleteTrainerOrderResponse(orderID);

    }

    @PutMapping(value = "/trainer-order-products/{orderID}/confirm")
    public TrainerOrderConfirmResponse ConfirmTrainerOrderResponse(@PathVariable int orderID){
        return trainerOrderService.ConfirmTrainerOrderResponse(orderID);

    }

    @PutMapping(value = "/trainer-order-products/{orderID}/reject")
    public TrainerOrderRejectResponse RejectTrainerOrderResponse(@PathVariable int orderID){
        return trainerOrderService.RejectTrainerOrderResponse(orderID);

    }

    @PutMapping(value = "/trainerOrder-addedToCart/{orderID}/confirm")
    public TrainerOrderAddToCartResponse AddToCartTrainerOrderResponse(@PathVariable int orderID){
        return trainerOrderService.AddToCartTrainerOrderResponse(orderID);

    }

    @PutMapping(value = "/trainerOrder-removedFromCart/{orderID}/confirm")
    public TrainerOrderRemovedFromCartResponse RemovedFromCartTrainerOrderResponse(@PathVariable int orderID){
        return trainerOrderService.RemovedFromCartTrainerOrderResponse(orderID);

    }

    @PutMapping(value = "/trainerOrder-payment/{orderID}/confirm")
    public TrainerOrderPaymentResponse PaymentTrainerOrderResponse(@PathVariable int orderID){
        return trainerOrderService.PaymentTrainerOrderResponse(orderID);

    }
}
