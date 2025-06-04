package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerSeedsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class ConsumerSeedsOrderController {

    @Autowired
    private ConsumerSeedsOrderService consumerSeedsOrderService;

    @PostMapping(path = "/consumerSeedsOrderProducts")
    public ConsumerSeedsOrderAddResponse save(@RequestBody ConsumerSeedsOrderDto consumerSeedsOrderDto) {
        return consumerSeedsOrderService.saveOrUpdate(consumerSeedsOrderDto);
    }

    @GetMapping("/viewConsumerSeedsOrders/{productID}")
    public ConsumerSeedsOrderGetResponse findBySFProduct_ProductID(@PathVariable("productID") int productID) {
        return consumerSeedsOrderService.getConsumerSeedsOrdersByProductId(productID);
    }

    @GetMapping("/viewConsumerSeedsOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public ConsumerSeedsOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return consumerSeedsOrderService.getConsumerSeedsOrderByUserId(userID);
    }

    @GetMapping("/viewConsumerOrdersBySeedsSellerID/{seedsSellerID}")
    public ConsumerSeedsOrderGetResponse findBySeedsSellerID(@PathVariable("seedsSellerID") int seedsSellerID) {
        return consumerSeedsOrderService.getConsumerSeedsOrderBySeedsSellerUserId(seedsSellerID);
    }

    @PutMapping(value = "/consumer-seedsOrder-products/{orderID}/delete")
    public ConsumerSeedsOrderDeleteResponse DeleteConsumerSeedsOrderResponse(@PathVariable int orderID){
        return consumerSeedsOrderService.DeleteConsumerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/consumer-seedsOrder-products/{orderID}/confirm")
    public ConsumerSeedsOrderConfirmResponse ConfirmConsumerSeedsOrderResponse(@PathVariable int orderID){
        return consumerSeedsOrderService.ConfirmConsumerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/consumer-seedsOrder-products/{orderID}/reject")
    public ConsumerSeedsOrderRejectResponse RejectConsumerSeedsOrderResponse(@PathVariable int orderID){
        return consumerSeedsOrderService.RejectConsumerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/consumerSeeds-addedToCart/{orderID}/confirm")
    public ConsumerSeedsOrderAddToCartResponse AddToCartConsumerSeedsOrderResponse(@PathVariable int orderID){
        return consumerSeedsOrderService.AddToCartConsumerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/consumerSeeds-removedFromCart/{orderID}/confirm")
    public ConsumerSeedsOrderRemovedFromCartResponse RemovedFromCartConsumerSeedsOrderResponse(@PathVariable int orderID){
        return consumerSeedsOrderService.RemovedFromCartConsumerSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/consumerSeeds-payment/{orderID}/confirm")
    public ConsumerSeedsOrderPaymentResponse PaymentConsumerSeedsOrderResponse(@PathVariable int orderID){
        return consumerSeedsOrderService.PaymentConsumerSeedsOrderResponse(orderID);

    }
}

