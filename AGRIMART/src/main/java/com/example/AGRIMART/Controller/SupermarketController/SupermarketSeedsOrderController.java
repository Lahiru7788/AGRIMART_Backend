package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Service.SupermarketService.SupermarketSeedsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class SupermarketSeedsOrderController {

    @Autowired
    private SupermarketSeedsOrderService supermarketSeedsOrderService;

    @PostMapping(path = "/supermarketSeedsOrderProducts")
    public SupermarketSeedsOrderAddResponse save(@RequestBody SupermarketSeedsOrderDto supermarketSeedsOrderDto) {
        return supermarketSeedsOrderService.saveOrUpdate(supermarketSeedsOrderDto);
    }

    @GetMapping("/viewSupermarketSeedsOrders/{productID}")
    public SupermarketSeedsOrderGetResponse findBySFProduct_ProductID(@PathVariable("productID") int productID) {
        return supermarketSeedsOrderService.getSupermarketSeedsOrdersByProductId(productID);
    }

    @GetMapping("/viewSupermarketSeedsOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public SupermarketSeedsOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return supermarketSeedsOrderService.getSupermarketSeedsOrderByUserId(userID);
    }

    @GetMapping("/viewSupermarketOrdersBySeedsSellerID/{seedsSellerID}")
    public SupermarketSeedsOrderGetResponse findBySeedsSellerID(@PathVariable("seedsSellerID") int seedsSellerID) {
        return supermarketSeedsOrderService.getSupermarketSeedsOrderBySeedsSellerUserId(seedsSellerID);
    }

    @PutMapping(value = "/supermarket-seedsOrder-products/{orderID}/delete")
    public SupermarketSeedsOrderDeleteResponse DeleteSupermarketSeedsOrderResponse(@PathVariable int orderID){
        return supermarketSeedsOrderService.DeleteSupermarketSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/supermarket-seedsOrder-products/{orderID}/confirm")
    public SupermarketSeedsOrderConfirmResponse ConfirmSupermarketSeedsOrderResponse(@PathVariable int orderID){
        return supermarketSeedsOrderService.ConfirmSupermarketSeedsOrderResponse(orderID);

    }

    @PutMapping(value = "/supermarket-seedsOrder-products/{orderID}/reject")
    public SupermarketSeedsOrderRejectResponse RejectSupermarketSeedsOrderResponse(@PathVariable int orderID){
        return supermarketSeedsOrderService.RejectSupermarketSeedsOrderResponse(orderID);

    }
}
