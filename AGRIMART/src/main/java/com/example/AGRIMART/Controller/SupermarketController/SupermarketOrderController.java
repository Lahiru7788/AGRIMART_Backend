package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class SupermarketOrderController {

    @Autowired
    private SupermarketOrderService supermarketOrderService;

    @PostMapping(path = "/supermarketOrderProducts")
    public SupermarketOrderAddResponse save(@RequestBody SupermarketOrderDto supermarketOrderDto) {
        return supermarketOrderService.saveOrUpdate(supermarketOrderDto);
    }

    @GetMapping("/viewSupermarketOrders/{productID}")
    public SupermarketOrderGetResponse findByFarmerProduct_ProductID(@PathVariable("productID") int productID) {
        return supermarketOrderService.getSupermarketOrdersByProductId(productID);
    }

    @GetMapping("/viewSupermarketOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public SupermarketOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return supermarketOrderService.getSupermarketOrderByUserId(userID);
    }

    @GetMapping("/viewSupermarketOrdersByFarmerID/{farmerID}")
    public SupermarketOrderGetResponse findByFarmerID(@PathVariable("farmerID") int farmerID) {
        return supermarketOrderService.getSupermarketOrderByFarmerUserId(farmerID);
    }

    @PutMapping(value = "/supermarket-order-products/{orderID}/delete")
    public SupermarketOrderDeleteResponse DeleteSupermarketOrderResponse(@PathVariable int orderID){
        return supermarketOrderService.DeleteSupermarketOrderResponse(orderID);

    }

    @PutMapping(value = "/supermarket-order-products/{orderID}/confirm")
    public SupermarketOrderConfirmResponse ConfirmSupermarketOrderResponse(@PathVariable int orderID){
        return supermarketOrderService.ConfirmSupermarketOrderResponse(orderID);

    }

    @PutMapping(value = "/supermarket-order-products/{orderID}/reject")
    public SupermarketOrderRejectResponse RejectSupermarketOrderResponse(@PathVariable int orderID){
        return supermarketOrderService.RejectSupermarketOrderResponse(orderID);

    }
}
