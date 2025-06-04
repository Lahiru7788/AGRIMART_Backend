package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderGetResponse;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketAddOrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class SupermarketAddOrderController {

    @Autowired
    private SupermarketAddOrderService supermarketAddOrderService;

//    @PostMapping(path = "/supermarketAddOrders")
//    public SupermarketAddOrderAddResponse save(@RequestBody SupermarketAddOrderDto supermarketAddOrderDto, HttpSession session){
//        SupermarketAddOrderAddResponse response = supermarketAddOrderService.saveOrUpdate(supermarketAddOrderDto);
//
//        if ("200".equals(response.getStatus())) {
//            session.setAttribute("productName", supermarketAddOrderDto.getProductName());
//        }
//
//        return response;
//    }

    @PostMapping(path = "/supermarketAddOrders")
    public SupermarketAddOrderAddResponse save(@Valid @RequestBody SupermarketAddOrderDto supermarketAddOrderDto, HttpSession session) {
        System.out.println("Received ConsumerAddOrderDto: " + supermarketAddOrderDto);
        SupermarketAddOrderAddResponse response = supermarketAddOrderService.saveOrUpdate(supermarketAddOrderDto);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Order ID from DTO: " + supermarketAddOrderDto.getOrderID());
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

    @GetMapping("/viewSupermarketAddOrders")

    public SupermarketAddOrderGetResponse GetAllSupermarketOrders() {
        return supermarketAddOrderService.GetAllSupermarketOrders();

    }

    @GetMapping("/viewSupermarketAddOrders/{userID}")
    public SupermarketAddOrderGetResponse findByUser_UserID(@PathVariable("userID") int userID) {
        return supermarketAddOrderService.getSupermarketAddOrderByUserId(userID);
    }

    @PutMapping(value = "/Supermarket-order/{orderID}/delete")
    public SupermarketAddOrderDeleteResponse DeleteSupermarketResponse(@PathVariable int orderID){
        return supermarketAddOrderService.DeleteSupermarketResponse(orderID);

    }


}
