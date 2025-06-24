package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerSeedsOrderService;
import com.example.AGRIMART.Service.FarmerService.FarmerSeedsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class FarmerSeedsOrderController {

    @Autowired
    private FarmerSeedsOrderService farmerSeedsOrderService;

    @PostMapping(path = "/farmerSeedsOrderProducts")
    public FarmerSeedsOrderAddResponse save(@RequestBody FarmerSeedsOrderDto farmerSeedsOrderDto) {
        return farmerSeedsOrderService.saveOrUpdate(farmerSeedsOrderDto);
    }

    @GetMapping("/viewFarmerSeedsOrders/{productID}")
    public FarmerSeedsOrderGetResponse findBySFProduct_ProductID(@PathVariable("productID") int productID) {
        return farmerSeedsOrderService.getFarmerSeedsOrdersByProductId(productID);
    }

    @GetMapping("/viewFarmerSeedsOrdersByFarmerID/{userID}")  // Changed the path to be more specific
    public FarmerSeedsOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return farmerSeedsOrderService.getFarmerSeedsOrderByUserId(userID);
    }

    @GetMapping("/viewFarmerOrdersBySeedsSellerID/{seedsSellerID}")
    public FarmerSeedsOrderGetResponse findBySeedsSellerID(@PathVariable("seedsSellerID") int seedsSellerID) {
        return farmerSeedsOrderService.getFarmerSeedsOrderBySeedsSellerUserId(seedsSellerID);
    }

    @PutMapping(value = "/farmer-seedsOrder-products/{orderID}/delete")
    public FarmerSeedsOrderDeleteResponse DeleteFarmerSeedsOrderResponse(@PathVariable int orderID){
        return farmerSeedsOrderService.DeleteFarmerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/farmer-seedsOrder-products/{orderID}/confirm")
    public FarmerSeedsOrderConfirmResponse ConfirmFarmerSeedsOrderResponse(@PathVariable int orderID){
        return farmerSeedsOrderService.ConfirmFarmerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/farmer-seedsOrder-products/{orderID}/reject")
    public FarmerSeedsOrderRejectResponse RejectFarmerSeedsOrderResponse(@PathVariable int orderID){
        return farmerSeedsOrderService.RejectFarmerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/farmerSeeds-addedToCart/{orderID}/confirm")
    public FarmerSeedsOrderAddToCartResponse AddToCartFarmerSeedsOrderResponse(@PathVariable int orderID){
        return farmerSeedsOrderService.AddToCartFarmerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/farmerSeeds-removedFromCart/{orderID}/confirm")
    public FarmerSeedsOrderRemovedFromCartResponse RemovedFromCartFarmerSeedsOrderResponse(@PathVariable int orderID){
        return farmerSeedsOrderService.RemovedFromCartFarmerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/farmerSeeds-payment/{orderID}/confirm")
    public FarmerSeedsOrderPaymentResponse PaymentFarmerSeedsOrderResponse(@PathVariable int orderID){
        return farmerSeedsOrderService.PaymentFarmerSeedsOrderResponse(orderID);

    }
}
