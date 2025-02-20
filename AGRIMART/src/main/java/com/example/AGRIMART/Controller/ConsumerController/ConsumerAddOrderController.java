package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class ConsumerAddOrderController {

    @Autowired
    private ConsumerAddOrderService consumerAddOrderService;

    @PostMapping(path = "/consumerAddOrders")
    public ConsumerAddOrderAddResponse save(@RequestBody ConsumerAddOrderDto consumerAddOrderDto, HttpSession session){
        ConsumerAddOrderAddResponse response = consumerAddOrderService.saveOrUpdate(consumerAddOrderDto);

        if ("200".equals(response.getStatus())) {
            session.setAttribute("productName", consumerAddOrderDto.getProductName());
        }

        return response;
    }

    @GetMapping("/viewConsumerAddOrders")

    public ConsumerAddOrderGetResponse getAllConsumerOrders() {
        return consumerAddOrderService.GetAllConsumerOrders();

    }
}
