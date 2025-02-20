package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerPreOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import com.example.AGRIMART.Service.ConsumerService.ConsumerPreOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class ConsumerPreOrderController {

    @Autowired
    private ConsumerPreOrderService consumerPreOrderService;

    @PostMapping(path = "/consumerPreOrders")
    public ConsumerPreOrderAddResponse save(@RequestBody ConsumerPreOrderDto consumerPreOrderDto, HttpSession session){
        ConsumerPreOrderAddResponse response = consumerPreOrderService.saveOrUpdate(consumerPreOrderDto);

        if ("200".equals(response.getStatus())) {
            session.setAttribute("productName", consumerPreOrderDto.getProductName());
        }

        return response;
    }

    @GetMapping("/viewConsumerPreOrders")

    public ConsumerPreOrderGetResponse getAllConsumerPreOrders() {
        return consumerPreOrderService.GetAllConsumerPreOrders();

    }
}
