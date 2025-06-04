package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerCourseOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerCourseOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerCourseOrderImpl implements ConsumerCourseOrderService {

    @Autowired
    private ConsumerCourseOrderRepository consumerCourseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerAddCourseRepository trainerAddCourseRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerCourseOrderAddResponse saveOrUpdate(ConsumerCourseOrderDto consumerCourseOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String courseName = (String) session.getAttribute("courseName");

        if (username == null || username.isEmpty()) {
            ConsumerCourseOrderAddResponse response = new ConsumerCourseOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerCourse> productOptional = trainerAddCourseRepository.findByCourseID(consumerCourseOrderDto.getCourseID());

        if (userOptional.isEmpty()) {
            ConsumerCourseOrderAddResponse response = new ConsumerCourseOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            ConsumerCourseOrderAddResponse response = new ConsumerCourseOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        TrainerCourse trainerCourse = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<ConsumerCourseOrder> existingOrders = consumerCourseOrderRepository.findById(consumerCourseOrderDto.getOrderID());

        ConsumerCourseOrder consumerCourseOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            consumerCourseOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            consumerCourseOrder = new ConsumerCourseOrder();
            consumerCourseOrder.setUser(user);
            consumerCourseOrder.setTrainerCourse(trainerCourse);
            actionPerformed = "added";
        }

        // Update fields
        consumerCourseOrder.setCourseName(consumerCourseOrderDto.getCourseName());
        consumerCourseOrder.setPrice(consumerCourseOrderDto.getPrice());
        consumerCourseOrder.setDescription(consumerCourseOrderDto.getDescription());
        consumerCourseOrder.setAddedDate(consumerCourseOrderDto.getAddedDate());
        consumerCourseOrder.setCourseCategory(ConsumerCourseOrderDto.CourseCategory.valueOf(consumerCourseOrderDto.getCourseCategory()));
        consumerCourseOrder.setActive(true);
        consumerCourseOrder.setUser(user);
        consumerCourseOrder.setTrainerCourse(trainerCourse);
        consumerCourseOrder.setAddedToCart(false);
        consumerCourseOrder.setRemovedFromCart(false);
        consumerCourseOrder.setPaid(false);

        ConsumerCourseOrderAddResponse response = new ConsumerCourseOrderAddResponse();
        try {
            // Save or update the offer
            ConsumerCourseOrder savedOrder = consumerCourseOrderRepository.save(consumerCourseOrder);

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

    public ConsumerCourseOrderGetResponse getConsumerCourseOrderByUserId(int userID) {
        ConsumerCourseOrderGetResponse response = new ConsumerCourseOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerCourseOrder> consumerCourseOrderList = consumerCourseOrderRepository.findByUser_UserID(userID);

            if (consumerCourseOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerCourseOrderDto> consumerCourseOrderDtoList = consumerCourseOrderList.stream()
                    .map(consumerCourseOrder -> {
                        ConsumerCourseOrderDto dto = new ConsumerCourseOrderDto();
                        dto.setOrderID(consumerCourseOrder.getOrderID());
                        dto.setCourseName(consumerCourseOrder.getCourseName());
                        dto.setPrice(consumerCourseOrder.getPrice());
                        dto.setAddedDate(consumerCourseOrder.getAddedDate());
                        dto.setDescription(consumerCourseOrder.getDescription());
                        dto.setActive(consumerCourseOrder.isActive());
                        dto.setAddedToCart(consumerCourseOrder.isAddedToCart());
                        dto.setRemovedFromCart(consumerCourseOrder.isRemovedFromCart());
                        dto.setPaid(consumerCourseOrder.isPaid());
                        dto.setCourseCategory(String.valueOf(consumerCourseOrder.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerCourseOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerCourseOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerCourseOrder.getUser().getFirstName());
                        userDto.setLastName(consumerCourseOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerCourseOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerAddCourseDto trainerAddCourseDto = new TrainerAddCourseDto();
                        trainerAddCourseDto.setCourseID(consumerCourseOrder.getTrainerCourse().getCourseID());
                        trainerAddCourseDto.setCourseName(consumerCourseOrder.getTrainerCourse().getCourseName());
                        trainerAddCourseDto.setPrice(consumerCourseOrder.getTrainerCourse().getPrice());
                        trainerAddCourseDto.setAddedDate(consumerCourseOrder.getTrainerCourse().getAddedDate());
                        trainerAddCourseDto.setDescription(consumerCourseOrder.getTrainerCourse().getDescription());
                        trainerAddCourseDto.setDeleted(consumerCourseOrder.getTrainerCourse().isDeleted());
                        trainerAddCourseDto.setCourseCategory(String.valueOf(consumerCourseOrder.getTrainerCourse().getCourseCategory()));

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(consumerCourseOrder.getTrainerCourse().getUser().getUserID());
                        trainerUserDto.setUserEmail(consumerCourseOrder.getTrainerCourse().getUser().getUserEmail());
                        trainerUserDto.setFirstName(consumerCourseOrder.getTrainerCourse().getUser().getFirstName());
                        trainerUserDto.setLastName(consumerCourseOrder.getTrainerCourse().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(consumerCourseOrder.getTrainerCourse().getUser().getUserType()));
                        trainerAddCourseDto.setUser(trainerUserDto);

                        dto.setTrainerCourse(trainerAddCourseDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerCourseOrderGetResponse(consumerCourseOrderDtoList);
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

    public ConsumerCourseOrderGetResponse getConsumerCourseOrderByTrainerUserId(int trainerUserID) {
        ConsumerCourseOrderGetResponse response = new ConsumerCourseOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<ConsumerCourseOrder> consumerCourseOrderList = consumerCourseOrderRepository.findByTrainerCourse_User_UserID(trainerUserID);

            if (consumerCourseOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No courses found for farmer ID: " + trainerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<ConsumerCourseOrderDto> consumerCourseOrderDtoList = consumerCourseOrderList.stream()
                    .map(consumerCourseOrder -> {
                        ConsumerCourseOrderDto dto = new ConsumerCourseOrderDto();
                        dto.setOrderID(consumerCourseOrder.getOrderID());
                        dto.setCourseName(consumerCourseOrder.getCourseName());
                        dto.setPrice(consumerCourseOrder.getPrice());
                        dto.setAddedDate(consumerCourseOrder.getAddedDate());
                        dto.setDescription(consumerCourseOrder.getDescription());
                        dto.setActive(consumerCourseOrder.isActive());
                        dto.setAddedToCart(consumerCourseOrder.isAddedToCart());
                        dto.setRemovedFromCart(consumerCourseOrder.isRemovedFromCart());
                        dto.setPaid(consumerCourseOrder.isPaid());
                        dto.setCourseCategory(String.valueOf(consumerCourseOrder.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerCourseOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerCourseOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerCourseOrder.getUser().getFirstName());
                        userDto.setLastName(consumerCourseOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerCourseOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerAddCourseDto trainerAddCourseDto = new TrainerAddCourseDto();
                        trainerAddCourseDto.setCourseID(consumerCourseOrder.getTrainerCourse().getCourseID());
                        trainerAddCourseDto.setCourseName(consumerCourseOrder.getTrainerCourse().getCourseName());
                        trainerAddCourseDto.setPrice(consumerCourseOrder.getTrainerCourse().getPrice());
                        trainerAddCourseDto.setAddedDate(consumerCourseOrder.getTrainerCourse().getAddedDate());
                        trainerAddCourseDto.setDescription(consumerCourseOrder.getTrainerCourse().getDescription());
                        trainerAddCourseDto.setDeleted(consumerCourseOrder.getTrainerCourse().isDeleted());
                        trainerAddCourseDto.setCourseCategory(String.valueOf(consumerCourseOrder.getTrainerCourse().getCourseCategory()));

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(consumerCourseOrder.getTrainerCourse().getUser().getUserID());
                        trainerUserDto.setUserEmail(consumerCourseOrder.getTrainerCourse().getUser().getUserEmail());
                        trainerUserDto.setFirstName(consumerCourseOrder.getTrainerCourse().getUser().getFirstName());
                        trainerUserDto.setLastName(consumerCourseOrder.getTrainerCourse().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(consumerCourseOrder.getTrainerCourse().getUser().getUserType()));
                        trainerAddCourseDto.setUser(trainerUserDto);

                        dto.setTrainerCourse(trainerAddCourseDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerCourseOrderGetResponse(consumerCourseOrderDtoList);
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


    public ConsumerCourseOrderGetResponse getConsumerCourseOrdersByCourseId(int courseID) {
        ConsumerCourseOrderGetResponse response = new ConsumerCourseOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerCourseOrder> consumerCourseOrderList = consumerCourseOrderRepository.findByTrainerCourse_CourseID(courseID);

            if (consumerCourseOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No courses found for user ID: " + courseID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerCourseOrderDto> consumerCourseOrderDtoList = consumerCourseOrderList.stream()
                    .map(consumerCourseOrder -> {
                        ConsumerCourseOrderDto dto = new ConsumerCourseOrderDto();
                        dto.setOrderID(consumerCourseOrder.getOrderID());
                        dto.setCourseName(consumerCourseOrder.getCourseName());
                        dto.setPrice(consumerCourseOrder.getPrice());
                        dto.setAddedDate(consumerCourseOrder.getAddedDate());
                        dto.setDescription(consumerCourseOrder.getDescription());
                        dto.setActive(consumerCourseOrder.isActive());
                        dto.setAddedToCart(consumerCourseOrder.isAddedToCart());
                        dto.setRemovedFromCart(consumerCourseOrder.isRemovedFromCart());
                        dto.setPaid(consumerCourseOrder.isPaid());
                        dto.setCourseCategory(String.valueOf(consumerCourseOrder.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerCourseOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerCourseOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerCourseOrder.getUser().getFirstName());
                        userDto.setLastName(consumerCourseOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerCourseOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerAddCourseDto trainerAddCourseDto = new TrainerAddCourseDto();
                        trainerAddCourseDto.setCourseID(consumerCourseOrder.getTrainerCourse().getCourseID());
                        trainerAddCourseDto.setCourseName(consumerCourseOrder.getTrainerCourse().getCourseName());
                        trainerAddCourseDto.setPrice(consumerCourseOrder.getTrainerCourse().getPrice());
                        trainerAddCourseDto.setAddedDate(consumerCourseOrder.getTrainerCourse().getAddedDate());
                        trainerAddCourseDto.setDescription(consumerCourseOrder.getTrainerCourse().getDescription());
                        trainerAddCourseDto.setDeleted(consumerCourseOrder.getTrainerCourse().isDeleted());
                        trainerAddCourseDto.setCourseCategory(String.valueOf(consumerCourseOrder.getTrainerCourse().getCourseCategory()));

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(consumerCourseOrder.getTrainerCourse().getUser().getUserID());
                        trainerUserDto.setUserEmail(consumerCourseOrder.getTrainerCourse().getUser().getUserEmail());
                        trainerUserDto.setFirstName(consumerCourseOrder.getTrainerCourse().getUser().getFirstName());
                        trainerUserDto.setLastName(consumerCourseOrder.getTrainerCourse().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(consumerCourseOrder.getTrainerCourse().getUser().getUserType()));
                        trainerAddCourseDto.setUser(trainerUserDto);

                        dto.setTrainerCourse(trainerAddCourseDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerCourseOrderGetResponse(consumerCourseOrderDtoList);
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
    public ConsumerCourseOrderDeleteResponse DeleteConsumerCourseOrderResponse(int orderID) {
        ConsumerCourseOrderDeleteResponse response = new ConsumerCourseOrderDeleteResponse();

        //calculation part
        ConsumerCourseOrder consumerCourseOrder;
        consumerCourseOrder = consumerCourseOrderRepository.findByOrderID(orderID);



        try {
            consumerCourseOrder.setActive(false);
            consumerCourseOrderRepository.save(consumerCourseOrder);
            response.setConsumerCourseOrderDeleteResponse(consumerCourseOrder);
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
    public ConsumerCourseOrderAddToCartResponse AddToCartConsumerCourseOrderResponse(int orderID) {
        ConsumerCourseOrderAddToCartResponse response = new ConsumerCourseOrderAddToCartResponse();

        //calculation part
        ConsumerCourseOrder consumerCourseOrder;
        consumerCourseOrder = consumerCourseOrderRepository.findByOrderID(orderID);



        try {
                consumerCourseOrder.setAddedToCart(true);
                consumerCourseOrderRepository.save(consumerCourseOrder);
                response.setConsumerCourseOrderAddToCartResponse(consumerCourseOrder);
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
    public ConsumerCourseOrderRemovedFromCartResponse RemovedFromCartConsumerCourseOrderResponse(int orderID) {
        ConsumerCourseOrderRemovedFromCartResponse response = new ConsumerCourseOrderRemovedFromCartResponse();

        //calculation part
        ConsumerCourseOrder consumerCourseOrder;
        consumerCourseOrder = consumerCourseOrderRepository.findByOrderID(orderID);



        try {
            consumerCourseOrder.setRemovedFromCart(true);
            consumerCourseOrderRepository.save(consumerCourseOrder);
            response.setConsumerCourseOrderRemovedFromCartResponse(consumerCourseOrder);
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
    public ConsumerCourseOrderPaymentResponse PaymentConsumerCourseOrderResponse(int orderID) {
        ConsumerCourseOrderPaymentResponse response = new ConsumerCourseOrderPaymentResponse();

        //calculation part
        ConsumerCourseOrder consumerCourseOrder;
        consumerCourseOrder = consumerCourseOrderRepository.findByOrderID(orderID);



        try {
            consumerCourseOrder.setPaid(true);
            consumerCourseOrderRepository.save(consumerCourseOrder);
            response.setConsumerCourseOrderPaymentResponse(consumerCourseOrder);
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



