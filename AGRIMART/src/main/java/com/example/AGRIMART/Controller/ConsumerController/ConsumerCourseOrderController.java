package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Service.ConsumerService.ConsumerCourseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RestController
@RequestMapping("/api/user")
public class ConsumerCourseOrderController {

    @Autowired
    private ConsumerCourseOrderService consumerCourseOrderService;

    @PostMapping(path = "/consumerCourseOrders")
    public ConsumerCourseOrderAddResponse save(@RequestBody ConsumerCourseOrderDto consumerCourseOrderDto) {
        return consumerCourseOrderService.saveOrUpdate(consumerCourseOrderDto);
    }

    @GetMapping("/viewConsumerCourseOrders/{courseID}")
    public ConsumerCourseOrderGetResponse findByTrainerCourse_CourseID(@PathVariable("courseID") int courseID) {
        return consumerCourseOrderService.getConsumerCourseOrdersByCourseId(courseID);
    }

    @GetMapping("/viewConsumerCourseOrdersByConsumerID/{userID}")  // Changed the path to be more specific
    public ConsumerCourseOrderGetResponse findByUserID(@PathVariable("userID") int userID) {
        return consumerCourseOrderService.getConsumerCourseOrderByUserId(userID);
    }

    @GetMapping("/viewConsumerCourseOrdersByTrainerID/{trainerID}")
    public ConsumerCourseOrderGetResponse findByTrainerID(@PathVariable("trainerID") int trainerID) {
        return consumerCourseOrderService.getConsumerCourseOrderByTrainerUserId(trainerID);
    }

    @PutMapping(value = "/consumerCourse-order-products/{orderID}/delete")
    public ConsumerCourseOrderDeleteResponse DeleteConsumerCourseOrderResponse(@PathVariable int orderID){
        return consumerCourseOrderService.DeleteConsumerCourseOrderResponse(orderID);

    }

    @PutMapping(value = "/consumerCourse-addedToCart/{orderID}/confirm")
    public ConsumerCourseOrderAddToCartResponse AddToCartConsumerCourseOrderResponse(@PathVariable int orderID){
        return consumerCourseOrderService.AddToCartConsumerCourseOrderResponse(orderID);

    }

    @PutMapping(value = "/consumerCourse-removedFromCart/{orderID}/confirm")
    public ConsumerCourseOrderRemovedFromCartResponse RemovedFromCartConsumerCourseOrderResponse(@PathVariable int orderID){
        return consumerCourseOrderService.RemovedFromCartConsumerCourseOrderResponse(orderID);

    }

    @PutMapping(value = "/consumerCourse-payment/{orderID}/confirm")
    public ConsumerCourseOrderPaymentResponse PaymentConsumerCourseOrderResponse(@PathVariable int orderID){
        return consumerCourseOrderService.PaymentConsumerCourseOrderResponse(orderID);

    }
}
