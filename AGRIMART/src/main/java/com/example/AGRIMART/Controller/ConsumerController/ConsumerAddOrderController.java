package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class ConsumerAddOrderController {

    @Autowired
    private ConsumerAddOrderService consumerAddOrderService;

//    @PostMapping(path = "/consumerAddOrders")
//    public ConsumerAddOrderAddResponse save(@RequestBody ConsumerAddOrderDto consumerAddOrderDto, HttpSession session){
//        ConsumerAddOrderAddResponse response = consumerAddOrderService.saveOrUpdate(consumerAddOrderDto);
//
//        if ("200".equals(response.getStatus())) {
//            session.setAttribute("productName", consumerAddOrderDto.getProductName());
//        }
//
//        return response;
//    }

    @PostMapping(path = "/consumerAddOrders")
    public ConsumerAddOrderAddResponse save(@Valid @RequestBody ConsumerAddOrderDto consumerAddOrderDto, HttpSession session) {
        System.out.println("Received ConsumerAddOrderDto: " + consumerAddOrderDto);
        ConsumerAddOrderAddResponse response = consumerAddOrderService.saveOrUpdate(consumerAddOrderDto);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Order ID from DTO: " + consumerAddOrderDto.getOrderID());
        System.out.println("Order ID from Response: " + response.getOrderID());
        if ("200".equals(response.getStatus()) && response.getOrderID() != null) {
            int orderID = response.getOrderID(); // Use auto-generated ID from response
            session.setAttribute("orderID", orderID);
            System.out.println("Order ID set in session: " + orderID);
        } else {
            System.out.println("Failed to set product ID in session due to status: " + response.getStatus());
        }
        System.out.println("Session orderID: " + session.getAttribute("orderID"));
        return response;
    }

    @GetMapping("/viewConsumerAddOrders")

    public ConsumerAddOrderGetResponse getAllConsumerOrders() {
        return consumerAddOrderService.GetAllConsumerOrders();

    }

    @GetMapping("/viewConsumerAddOrders/{userID}")
    public ConsumerAddOrderGetResponse findByUser_UserID(@PathVariable("userID") int userID) {
        return consumerAddOrderService.getConsumerAddOrderByUserId(userID);
    }

    @PutMapping(value = "/consumer-order/{orderID}/delete")
    public ConsumerAddOrderDeleteResponse DeleteConsumerResponse(@PathVariable int orderID){
        return consumerAddOrderService.DeleteConsumerResponse(orderID);

    }
}
