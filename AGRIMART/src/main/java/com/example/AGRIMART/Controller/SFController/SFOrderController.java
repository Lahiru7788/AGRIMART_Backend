package com.example.AGRIMART.Controller.SFController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SFResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import com.example.AGRIMART.Service.SFService.SFOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class SFOrderController {

    @Autowired
    private SFOrderService sfOrderService;

    @PostMapping(path = "/sfOrderProducts")
    public SFOrderAddResponse save(@RequestBody SFOrderDto sfOrderDto) {
        return sfOrderService.saveOrUpdate(sfOrderDto);
    }

    @GetMapping("/viewSFOrders/{productID}")
    public SFOrderGetResponse findBySFProduct_ProductID(@PathVariable("productID") int productID) {
        return sfOrderService.getSFOrdersByProductId(productID);
    }

    @GetMapping("/viewSFOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public SFOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return sfOrderService.getSFOrderByUserId(userID);
    }

    @GetMapping("/viewSFOrdersByFarmerID/{farmerID}")
    public SFOrderGetResponse findByFarmerID(@PathVariable("farmerID") int farmerID) {
        return sfOrderService.getSFOrderByFarmerUserId(farmerID);
    }

    @PutMapping(value = "/sf-order-products/{orderID}/delete")
    public SFOrderDeleteResponse DeleteSFOrderResponse(@PathVariable int orderID){
        return sfOrderService.DeleteSFOrderResponse(orderID);

    }

    @PutMapping(value = "/sf-order-products/{orderID}/confirm")
    public SFOrderConfirmResponse ConfirmSFOrderResponse(@PathVariable int orderID){
        return sfOrderService.ConfirmSFOrderResponse(orderID);

    }

    @PutMapping(value = "/sf-order-products/{orderID}/reject")
    public SFOrderRejectResponse RejectSFOrderResponse(@PathVariable int orderID){
        return sfOrderService.RejectSFOrderResponse(orderID);

    }

    @PutMapping(value = "/sf-addedToCart/{orderID}/confirm")
    public SFOrderAddToCartResponse AddToCartSFOrderResponse(@PathVariable int orderID){
        return sfOrderService.AddToCartSFOrderResponse(orderID);

    }

    @PutMapping(value = "/sf-removedFromCart/{orderID}/confirm")
    public SFOrderRemovedFromCartResponse RemovedFromCartSFOrderResponse(@PathVariable int orderID){
        return sfOrderService.RemovedFromCartSFOrderResponse(orderID);

    }

    @PutMapping(value = "/sf-payment/{orderID}/confirm")
    public SFOrderPaymentResponse PaymentSFOrderResponse(@PathVariable int orderID){
        return sfOrderService.PaymentSFOrderResponse(orderID);

    }
}
