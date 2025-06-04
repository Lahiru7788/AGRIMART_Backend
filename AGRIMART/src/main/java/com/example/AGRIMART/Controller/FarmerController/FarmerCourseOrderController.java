package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerCourseOrderDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerCourseOrderService;
import com.example.AGRIMART.Service.FarmerService.FarmerCourseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class FarmerCourseOrderController {

    @Autowired
    private FarmerCourseOrderService farmerCourseOrderService;

    @PostMapping(path = "/farmerCourseOrders")
    public FarmerCourseOrderAddResponse save(@RequestBody FarmerCourseOrderDto farmerCourseOrderDto) {
        return farmerCourseOrderService.saveOrUpdate(farmerCourseOrderDto);
    }

    @GetMapping("/viewFarmerCourseOrders/{courseID}")
    public FarmerCourseOrderGetResponse findByTrainerCourse_CourseID(@PathVariable("courseID") int courseID) {
        return farmerCourseOrderService.getFarmerCourseOrdersByCourseId(courseID);
    }

    @GetMapping("/viewFarmerCourseOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public FarmerCourseOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return farmerCourseOrderService.getFarmerCourseOrderByUserId(userID);
    }

    @GetMapping("/viewFarmerCourseOrdersByTrainerID/{trainerID}")
    public FarmerCourseOrderGetResponse findByTrainerID(@PathVariable("trainerID") int trainerID) {
        return farmerCourseOrderService.getFarmerCourseOrderByTrainerUserId(trainerID);
    }

    @PutMapping(value = "/farmerCourse-order-products/{orderID}/delete")
    public FarmerCourseOrderDeleteResponse DeleteFarmerCourseOrderResponse(@PathVariable int orderID){
        return farmerCourseOrderService.DeleteFarmerCourseOrderResponse(orderID);

    }

    @PutMapping(value = "/farmerCourse-addedToCart/{orderID}/confirm")
    public FarmerCourseOrderAddToCartResponse AddToCartFarmerCourseOrderResponse(@PathVariable int orderID){
        return farmerCourseOrderService.AddToCartFarmerCourseOrderResponse(orderID);

    }

    @PutMapping(value = "/farmerCourse-removedFromCart/{orderID}/confirm")
    public FarmerCourseOrderRemovedFromCartResponse RemovedFromCartFarmerCourseOrderResponse(@PathVariable int orderID){
        return farmerCourseOrderService.RemovedFromCartFarmerCourseOrderResponse(orderID);

    }

    @PutMapping(value = "/farmerCourse-payment/{orderID}/confirm")
    public FarmerCourseOrderPaymentResponse PaymentFarmerCourseOrderResponse(@PathVariable int orderID){
        return farmerCourseOrderService.PaymentFarmerCourseOrderResponse(orderID);

    }
}
