package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class ConsumerOrderController {

    @Autowired
    private ConsumerOrderService consumerOrderService;

    @PostMapping(path = "/consumerOrderProducts")
    public ConsumerOrderAddResponse save(@RequestBody ConsumerOrderDto consumerOrderDto) {
        return consumerOrderService.saveOrUpdate(consumerOrderDto);
    }

    @GetMapping("/viewConsumerOrders/{productID}")
    public ConsumerOrderGetResponse findByFarmerProduct_ProductID(@PathVariable("productID") int productID) {
        return consumerOrderService.getConsumerOrdersByProductId(productID);
    }

    @GetMapping("/viewConsumerOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public ConsumerOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return consumerOrderService.getConsumerOrderByUserId(userID);
    }

    @GetMapping("/viewConsumerOrdersByFarmerID/{farmerID}")
    public ConsumerOrderGetResponse findByFarmerID(@PathVariable("farmerID") int farmerID) {
        return consumerOrderService.getConsumerOrderByFarmerUserId(farmerID);
    }

    @PutMapping(value = "/consumer-order-products/{orderID}/delete")
    public ConsumerOrderDeleteResponse DeleteConsumerOrderResponse(@PathVariable int orderID){
        return consumerOrderService.DeleteConsumerOrderResponse(orderID);

    }

    @PutMapping(value = "/consumer-order-products/{orderID}/confirm")
    public ConsumerOrderConfirmResponse ConfirmConsumerOrderResponse(@PathVariable int orderID){
        return consumerOrderService.ConfirmConsumerOrderResponse(orderID);

    }

    @PutMapping(value = "/consumer-order-products/{orderID}/reject")
    public ConsumerOrderRejectResponse RejectConsumerOrderResponse(@PathVariable int orderID){
        return consumerOrderService.RejectConsumerOrderResponse(orderID);

    }

    @PutMapping(value = "/consumer-addedToCart/{orderID}/confirm")
    public ConsumerOrderAddToCartResponse AddToCartConsumerOrderResponse(@PathVariable int orderID){
        return consumerOrderService.AddToCartConsumerOrderResponse(orderID);

    }

    @PutMapping(value = "/consumer-removedFromCart/{orderID}/confirm")
    public ConsumerOrderRemovedFromCartResponse RemovedFromCartConsumerOrderResponse(@PathVariable int orderID){
        return consumerOrderService.RemovedFromCartConsumerOrderResponse(orderID);

    }
}
