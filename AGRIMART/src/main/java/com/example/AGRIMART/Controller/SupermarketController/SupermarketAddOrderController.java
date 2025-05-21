package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderGetResponse;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketAddOrderService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping(path = "/supermarketAddOrders")
    public SupermarketAddOrderAddResponse save(@RequestBody SupermarketAddOrderDto supermarketAddOrderDto, HttpSession session){
        SupermarketAddOrderAddResponse response = supermarketAddOrderService.saveOrUpdate(supermarketAddOrderDto);

        if ("200".equals(response.getStatus())) {
            session.setAttribute("productName", supermarketAddOrderDto.getProductName());
        }

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
