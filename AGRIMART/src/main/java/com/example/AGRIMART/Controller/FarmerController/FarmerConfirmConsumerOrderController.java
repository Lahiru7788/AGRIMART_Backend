package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmConsumerOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Service.FarmerService.FarmerConfirmConsumerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class FarmerConfirmConsumerOrderController {

    @Autowired
    private FarmerConfirmConsumerOrderService farmerConfirmConsumerOrderService;

    @PostMapping(path = "/farmerAcceptConsumerOrders")
    public FarmerConfirmConsumerOrderAddResponse save(@RequestBody FarmerConfirmConsumerOrderDto farmerConfirmConsumerOrderDto) {
        return farmerConfirmConsumerOrderService.saveOrUpdate(farmerConfirmConsumerOrderDto);
    }

    @GetMapping("/viewConsumerProductOrders/{productID}")
    public FarmerConfirmConsumerOrderGetResponse findByConsumerAddOrder_OrderID(@PathVariable("orderID") int orderID) {
        return farmerConfirmConsumerOrderService.getFarmerConfirmConsumerOrderByOrderId(orderID);
    }

    @GetMapping("/viewConsumerProductOrdersByFarmerID/{userID}")  // Changed the path to be more specific
    public FarmerConfirmConsumerOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return farmerConfirmConsumerOrderService.getFarmerConfirmConsumerOrderByUserId(userID);
    }

    @GetMapping("/viewConsumerProductOrdersByConsumerID/{consumerID}")
    public FarmerConfirmConsumerOrderGetResponse findByConsumerID(@PathVariable("consumerID") int consumerID) {
        return farmerConfirmConsumerOrderService.getFarmerConfirmConsumerOrderByConsumerUserId(consumerID);
    }

    @PutMapping(value = "/consumerOrder-products/{confirmOrderID}/delete")
    public FarmerConfirmConsumerOrderDeleteResponse DeleteFarmerConfirmConsumerOrderResponse(@PathVariable int confirmOrderID){
        return farmerConfirmConsumerOrderService.DeleteFarmerConfirmConsumerOrderResponse(confirmOrderID);

    }

    @PutMapping(value = "/consumerOrder-payment/{confirmOrderID}/confirm")
    public FarmerConfirmConsumerOrderPaymentResponse PaymentFarmerConfirmConsumerOrderResponse(@PathVariable int confirmOrderID){
        return farmerConfirmConsumerOrderService.PaymentFarmerConfirmConsumerOrderResponse(confirmOrderID);

    }
}
