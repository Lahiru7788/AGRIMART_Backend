package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerCourseOrderDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerCourseOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerCourseOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerCourseOrderRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerCourseOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerCourseOrderImpl implements FarmerCourseOrderService {

    @Autowired
    private FarmerCourseOrderRepository farmerCourseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerAddCourseRepository trainerAddCourseRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerCourseOrderAddResponse saveOrUpdate(FarmerCourseOrderDto farmerCourseOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String courseName = (String) session.getAttribute("courseName");

        if (username == null || username.isEmpty()) {
            FarmerCourseOrderAddResponse response = new FarmerCourseOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerCourse> productOptional = trainerAddCourseRepository.findByCourseID(farmerCourseOrderDto.getCourseID());

        if (userOptional.isEmpty()) {
            FarmerCourseOrderAddResponse response = new FarmerCourseOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            FarmerCourseOrderAddResponse response = new FarmerCourseOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        TrainerCourse trainerCourse = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<FarmerCourseOrder> existingOrders = farmerCourseOrderRepository.findById(farmerCourseOrderDto.getOrderID());

        FarmerCourseOrder farmerCourseOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            farmerCourseOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            farmerCourseOrder = new FarmerCourseOrder();
            farmerCourseOrder.setUser(user);
            farmerCourseOrder.setTrainerCourse(trainerCourse);
            actionPerformed = "added";
        }

        // Update fields
        farmerCourseOrder.setCourseName(farmerCourseOrderDto.getCourseName());
        farmerCourseOrder.setPrice(farmerCourseOrderDto.getPrice());
        farmerCourseOrder.setDescription(farmerCourseOrderDto.getDescription());
        farmerCourseOrder.setAddedDate(farmerCourseOrderDto.getAddedDate());
        farmerCourseOrder.setCourseCategory(FarmerCourseOrderDto.CourseCategory.valueOf(farmerCourseOrderDto.getCourseCategory()));
        farmerCourseOrder.setActive(true);
        farmerCourseOrder.setUser(user);
        farmerCourseOrder.setTrainerCourse(trainerCourse);
        farmerCourseOrder.setAddedToCart(false);
        farmerCourseOrder.setRemovedFromCart(false);
        farmerCourseOrder.setPaid(false);

        FarmerCourseOrderAddResponse response = new FarmerCourseOrderAddResponse();
        try {
            // Save or update the offer
            FarmerCourseOrder savedOrder = farmerCourseOrderRepository.save(farmerCourseOrder);

            if (savedOrder != null) {
                response.setMessage("Order was " + actionPerformed + " successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to " + actionPerformed + " Order.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public FarmerCourseOrderGetResponse getFarmerCourseOrderByUserId(int userID) {
        FarmerCourseOrderGetResponse response = new FarmerCourseOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerCourseOrder> farmerCourseOrderList = farmerCourseOrderRepository.findByUser_UserID(userID);

            if (farmerCourseOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerCourseOrderDto> farmerCourseOrderDtoList = farmerCourseOrderList.stream()
                    .map(farmerCourseOrder -> {
                        FarmerCourseOrderDto dto = new FarmerCourseOrderDto();
                        dto.setOrderID(farmerCourseOrder.getOrderID());
                        dto.setCourseName(farmerCourseOrder.getCourseName());
                        dto.setPrice(farmerCourseOrder.getPrice());
                        dto.setAddedDate(farmerCourseOrder.getAddedDate());
                        dto.setDescription(farmerCourseOrder.getDescription());
                        dto.setActive(farmerCourseOrder.isActive());
                        dto.setAddedToCart(farmerCourseOrder.isAddedToCart());
                        dto.setRemovedFromCart(farmerCourseOrder.isRemovedFromCart());
                        dto.setPaid(farmerCourseOrder.isPaid());
                        dto.setCourseCategory(String.valueOf(farmerCourseOrder.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerCourseOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerCourseOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerCourseOrder.getUser().getFirstName());
                        userDto.setLastName(farmerCourseOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerCourseOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerAddCourseDto trainerAddCourseDto = new TrainerAddCourseDto();
                        trainerAddCourseDto.setCourseID(farmerCourseOrder.getTrainerCourse().getCourseID());
                        trainerAddCourseDto.setCourseName(farmerCourseOrder.getTrainerCourse().getCourseName());
                        trainerAddCourseDto.setPrice(farmerCourseOrder.getTrainerCourse().getPrice());
                        trainerAddCourseDto.setAddedDate(farmerCourseOrder.getTrainerCourse().getAddedDate());
                        trainerAddCourseDto.setDescription(farmerCourseOrder.getTrainerCourse().getDescription());
                        trainerAddCourseDto.setDeleted(farmerCourseOrder.getTrainerCourse().isDeleted());
                        trainerAddCourseDto.setCourseCategory(String.valueOf(farmerCourseOrder.getTrainerCourse().getCourseCategory()));

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(farmerCourseOrder.getTrainerCourse().getUser().getUserID());
                        trainerUserDto.setUserEmail(farmerCourseOrder.getTrainerCourse().getUser().getUserEmail());
                        trainerUserDto.setFirstName(farmerCourseOrder.getTrainerCourse().getUser().getFirstName());
                        trainerUserDto.setLastName(farmerCourseOrder.getTrainerCourse().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(farmerCourseOrder.getTrainerCourse().getUser().getUserType()));
                        trainerAddCourseDto.setUser(trainerUserDto);

                        dto.setTrainerCourse(trainerAddCourseDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerCourseOrderGetResponse(farmerCourseOrderDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    public FarmerCourseOrderGetResponse getFarmerCourseOrderByTrainerUserId(int trainerUserID) {
        FarmerCourseOrderGetResponse response = new FarmerCourseOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<FarmerCourseOrder> farmerCourseOrderList = farmerCourseOrderRepository.findByTrainerCourse_User_UserID(trainerUserID);

            if (farmerCourseOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No courses found for farmer ID: " + trainerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<FarmerCourseOrderDto> farmerCourseOrderDtoList = farmerCourseOrderList.stream()
                    .map(farmerCourseOrder -> {
                        FarmerCourseOrderDto dto = new FarmerCourseOrderDto();
                        dto.setOrderID(farmerCourseOrder.getOrderID());
                        dto.setCourseName(farmerCourseOrder.getCourseName());
                        dto.setPrice(farmerCourseOrder.getPrice());
                        dto.setAddedDate(farmerCourseOrder.getAddedDate());
                        dto.setDescription(farmerCourseOrder.getDescription());
                        dto.setActive(farmerCourseOrder.isActive());
                        dto.setAddedToCart(farmerCourseOrder.isAddedToCart());
                        dto.setRemovedFromCart(farmerCourseOrder.isRemovedFromCart());
                        dto.setPaid(farmerCourseOrder.isPaid());
                        dto.setCourseCategory(String.valueOf(farmerCourseOrder.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerCourseOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerCourseOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerCourseOrder.getUser().getFirstName());
                        userDto.setLastName(farmerCourseOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerCourseOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerAddCourseDto trainerAddCourseDto = new TrainerAddCourseDto();
                        trainerAddCourseDto.setCourseID(farmerCourseOrder.getTrainerCourse().getCourseID());
                        trainerAddCourseDto.setCourseName(farmerCourseOrder.getTrainerCourse().getCourseName());
                        trainerAddCourseDto.setPrice(farmerCourseOrder.getTrainerCourse().getPrice());
                        trainerAddCourseDto.setAddedDate(farmerCourseOrder.getTrainerCourse().getAddedDate());
                        trainerAddCourseDto.setDescription(farmerCourseOrder.getTrainerCourse().getDescription());
                        trainerAddCourseDto.setDeleted(farmerCourseOrder.getTrainerCourse().isDeleted());
                        trainerAddCourseDto.setCourseCategory(String.valueOf(farmerCourseOrder.getTrainerCourse().getCourseCategory()));

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(farmerCourseOrder.getTrainerCourse().getUser().getUserID());
                        trainerUserDto.setUserEmail(farmerCourseOrder.getTrainerCourse().getUser().getUserEmail());
                        trainerUserDto.setFirstName(farmerCourseOrder.getTrainerCourse().getUser().getFirstName());
                        trainerUserDto.setLastName(farmerCourseOrder.getTrainerCourse().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(farmerCourseOrder.getTrainerCourse().getUser().getUserType()));
                        trainerAddCourseDto.setUser(trainerUserDto);

                        dto.setTrainerCourse(trainerAddCourseDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerCourseOrderGetResponse(farmerCourseOrderDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }


    public FarmerCourseOrderGetResponse getFarmerCourseOrdersByCourseId(int courseID) {
        FarmerCourseOrderGetResponse response = new FarmerCourseOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerCourseOrder> farmerCourseOrderList = farmerCourseOrderRepository.findByTrainerCourse_CourseID(courseID);

            if (farmerCourseOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No courses found for user ID: " + courseID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerCourseOrderDto> farmerCourseOrderDtoList = farmerCourseOrderList.stream()
                    .map(farmerCourseOrder -> {
                        FarmerCourseOrderDto dto = new FarmerCourseOrderDto();
                        dto.setOrderID(farmerCourseOrder.getOrderID());
                        dto.setCourseName(farmerCourseOrder.getCourseName());
                        dto.setPrice(farmerCourseOrder.getPrice());
                        dto.setAddedDate(farmerCourseOrder.getAddedDate());
                        dto.setDescription(farmerCourseOrder.getDescription());
                        dto.setActive(farmerCourseOrder.isActive());
                        dto.setAddedToCart(farmerCourseOrder.isAddedToCart());
                        dto.setRemovedFromCart(farmerCourseOrder.isRemovedFromCart());
                        dto.setPaid(farmerCourseOrder.isPaid());
                        dto.setCourseCategory(String.valueOf(farmerCourseOrder.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerCourseOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerCourseOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerCourseOrder.getUser().getFirstName());
                        userDto.setLastName(farmerCourseOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerCourseOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerAddCourseDto trainerAddCourseDto = new TrainerAddCourseDto();
                        trainerAddCourseDto.setCourseID(farmerCourseOrder.getTrainerCourse().getCourseID());
                        trainerAddCourseDto.setCourseName(farmerCourseOrder.getTrainerCourse().getCourseName());
                        trainerAddCourseDto.setPrice(farmerCourseOrder.getTrainerCourse().getPrice());
                        trainerAddCourseDto.setAddedDate(farmerCourseOrder.getTrainerCourse().getAddedDate());
                        trainerAddCourseDto.setDescription(farmerCourseOrder.getTrainerCourse().getDescription());
                        trainerAddCourseDto.setDeleted(farmerCourseOrder.getTrainerCourse().isDeleted());
                        trainerAddCourseDto.setCourseCategory(String.valueOf(farmerCourseOrder.getTrainerCourse().getCourseCategory()));

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(farmerCourseOrder.getTrainerCourse().getUser().getUserID());
                        trainerUserDto.setUserEmail(farmerCourseOrder.getTrainerCourse().getUser().getUserEmail());
                        trainerUserDto.setFirstName(farmerCourseOrder.getTrainerCourse().getUser().getFirstName());
                        trainerUserDto.setLastName(farmerCourseOrder.getTrainerCourse().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(farmerCourseOrder.getTrainerCourse().getUser().getUserType()));
                        trainerAddCourseDto.setUser(trainerUserDto);

                        dto.setTrainerCourse(trainerAddCourseDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerCourseOrderGetResponse(farmerCourseOrderDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public FarmerCourseOrderDeleteResponse DeleteFarmerCourseOrderResponse(int orderID) {
        FarmerCourseOrderDeleteResponse response = new FarmerCourseOrderDeleteResponse();

        //calculation part
        FarmerCourseOrder farmerCourseOrder;
        farmerCourseOrder = farmerCourseOrderRepository.findByOrderID(orderID);



        try {
            farmerCourseOrder.setActive(false);
            farmerCourseOrderRepository.save(farmerCourseOrder);
            response.setFarmerCourseOrderDeleteResponse(farmerCourseOrder);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }



    @Override
    public FarmerCourseOrderAddToCartResponse AddToCartFarmerCourseOrderResponse(int orderID) {
        FarmerCourseOrderAddToCartResponse response = new FarmerCourseOrderAddToCartResponse();

        FarmerCourseOrder farmerCourseOrder;
        farmerCourseOrder = farmerCourseOrderRepository.findByOrderID(orderID);



        try {
            farmerCourseOrder.setAddedToCart(true);
            farmerCourseOrderRepository.save(farmerCourseOrder);
            response.setFarmerCourseOrderAddToCartResponse(farmerCourseOrder);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");
        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerCourseOrderRemovedFromCartResponse RemovedFromCartFarmerCourseOrderResponse(int orderID) {
        FarmerCourseOrderRemovedFromCartResponse response = new FarmerCourseOrderRemovedFromCartResponse();

        FarmerCourseOrder farmerCourseOrder;
        farmerCourseOrder = farmerCourseOrderRepository.findByOrderID(orderID);



        try {
            farmerCourseOrder.setRemovedFromCart(true);
            farmerCourseOrderRepository.save(farmerCourseOrder);
            response.setFarmerCourseOrderRemovedFromCartResponse(farmerCourseOrder);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerCourseOrderPaymentResponse PaymentFarmerCourseOrderResponse(int orderID) {
        FarmerCourseOrderPaymentResponse response = new FarmerCourseOrderPaymentResponse();

        //calculation part
        FarmerCourseOrder farmerCourseOrder;
        farmerCourseOrder = farmerCourseOrderRepository.findByOrderID(orderID);


        try {
            farmerCourseOrder.setPaid(true);
            farmerCourseOrderRepository.save(farmerCourseOrder);
            response.setFarmerCourseOrderPaymentResponse(farmerCourseOrder);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }
}
