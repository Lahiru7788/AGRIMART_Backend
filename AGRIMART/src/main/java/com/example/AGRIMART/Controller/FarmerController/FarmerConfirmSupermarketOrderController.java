package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmSupermarketOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Service.FarmerService.FarmerConfirmSupermarketOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class FarmerConfirmSupermarketOrderController {

    @Autowired
    private FarmerConfirmSupermarketOrderService farmerConfirmSupermarketOrderService;

    @PostMapping(path = "/farmerAcceptSupermarketOrders")
    public FarmerConfirmSupermarketOrderAddResponse save(@RequestBody FarmerConfirmSupermarketOrderDto farmerConfirmSupermarketOrderDto) {
        return farmerConfirmSupermarketOrderService.saveOrUpdate(farmerConfirmSupermarketOrderDto);
    }

    @GetMapping("/viewSupermarketProductOrders/{productID}")
    public FarmerConfirmSupermarketOrderGetResponse findBySupermarketAddOrder_OrderID(@PathVariable("orderID") int orderID) {
        return farmerConfirmSupermarketOrderService.getFarmerConfirmSupermarketOrderByOrderId(orderID);
    }

    @GetMapping("/viewSupermarketProductOrdersByFarmerID/{userID}")  // Changed the path to be more specific
    public FarmerConfirmSupermarketOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return farmerConfirmSupermarketOrderService.getFarmerConfirmSupermarketOrderByUserId(userID);
    }

    @GetMapping("/viewSupermarketProductOrdersBySupermarketID/{supermarketID}")
    public FarmerConfirmSupermarketOrderGetResponse findBySupermarketID(@PathVariable("supermarketID") int supermarketID) {
        return farmerConfirmSupermarketOrderService.getFarmerConfirmSupermarketOrderBySupermarketUserId(supermarketID);
    }

    @PutMapping(value = "/supermarketOrder-products/{confirmOrderID}/delete")
    public FarmerConfirmSupermarketOrderDeleteResponse DeleteFarmerConfirmSupermarketOrderResponse(@PathVariable int confirmOrderID){
        return farmerConfirmSupermarketOrderService.DeleteFarmerConfirmSupermarketOrderResponse(confirmOrderID);

    }

    @PutMapping(value = "/supermarketOrder-payment/{confirmOrderID}/confirm")
    public FarmerConfirmSupermarketOrderPaymentResponse PaymentFarmerConfirmSupermarketOrderResponse(@PathVariable int confirmOrderID){
        return farmerConfirmSupermarketOrderService.PaymentFarmerConfirmSupermarketOrderResponse(confirmOrderID);

    }
}
