package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOfferDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferGetResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOfferAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOfferGetResponse;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOfferService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class SupermarketAddOfferController {

    @Autowired
    private SupermarketOfferService supermarketOfferService;

    @PostMapping(path = "/supermarketOffers")
    public SupermarketOfferAddResponse save(@RequestBody SupermarketOfferDto supermarketOfferDto){
        return supermarketOfferService.saveOrUpdate(supermarketOfferDto);
    }
//
//    @GetMapping("/viewConsumerOffers")
//
//    public ConsumerOfferGetResponse getAllConsumerOffers() {
//        return consumerOfferService.GetAllConsumerOffers();
//
//    }

    @GetMapping("/viewSupermarketOffers/{orderID}")
    public SupermarketOfferGetResponse findBySupermarketAddOrder_OrderID(@PathVariable("orderID") int orderID) {
        return supermarketOfferService.getSupermarketAddOffersByOrderId(orderID);
    }
}
