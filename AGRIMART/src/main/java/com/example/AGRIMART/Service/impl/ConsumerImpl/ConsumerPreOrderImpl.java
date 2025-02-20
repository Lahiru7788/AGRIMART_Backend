package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerPreOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerPreOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerPreOrderGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerPreOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerPreOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerPreOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerPreOrderImpl implements ConsumerPreOrderService {

    @Autowired
    private ConsumerPreOrderRepository consumerPreOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerPreOrderAddResponse saveOrUpdate(ConsumerPreOrderDto consumerPreOrderDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            ConsumerPreOrderAddResponse response = new ConsumerPreOrderAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            ConsumerPreOrderAddResponse response = new ConsumerPreOrderAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<ConsumerPreOrder> existingProductOptional = consumerPreOrderRepository.findById(consumerPreOrderDto.getPreOrderID());

        ConsumerPreOrder consumerPreOrder = existingProductOptional.orElse(new ConsumerPreOrder());
        // If product exists, we update the fields; if not, we create a new one
        consumerPreOrder.setProductName(consumerPreOrderDto.getProductName());
        consumerPreOrder.setPrice(consumerPreOrderDto.getPrice());
        consumerPreOrder.setDescription(consumerPreOrderDto.getDescription());
        consumerPreOrder.setRequiredQuantity(consumerPreOrderDto.getRequiredQuantity());
        consumerPreOrder.setRequiredTime(consumerPreOrderDto.getRequiredTime());
        consumerPreOrder.setAddedDate(consumerPreOrderDto.getAddedDate());
        consumerPreOrder.setProductCategory(ConsumerAddOrderDto.ProductCategory.valueOf(consumerPreOrderDto.getProductCategory()));

        // Ensure the user is assigned
        consumerPreOrder.setUser(user);

        // Set the flags based on business rules
        consumerPreOrder.setActive(true);
        consumerPreOrder.setConfirmed(false);


        ConsumerPreOrderAddResponse response = new ConsumerPreOrderAddResponse();
        try {
            // Save or update the product
            ConsumerPreOrder savedPreOrder = consumerPreOrderRepository.save(consumerPreOrder);

            if (savedPreOrder != null) {
                response.setMessage("Order was saved/updated successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to save/update Order.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }



    //    @Override
//    public UserDetailsGetResponse GetAllUserDetails() {
//        UserDetailsGetResponse response = new UserDetailsGetResponse();
//        try {
//            List<UserDetails> userDetails =userDetailsRepository.findAll();
//            response.setUserDetailsGetResponse(userDetails);
//            response.setStatus("200");
//            response.setMessage("User Details retrieved successfully");
//            response.setResponseCode("1600");
//
//        }catch (Exception e){
//            response.setStatus("500");
//            response.setMessage("Error retrieving User Details: " + e.getMessage());
//            response.setResponseCode("1601");
//
//        }
//
//        return response;
//    }
    public ConsumerPreOrderGetResponse GetAllConsumerPreOrders() {
        ConsumerPreOrderGetResponse response = new ConsumerPreOrderGetResponse();
        try {
            // Fetch all user details
            List<ConsumerPreOrder> consumerPreOrderList = consumerPreOrderRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<ConsumerPreOrderDto> consumerPreOrderDtoList = consumerPreOrderList.stream()
                    .map(consumerPreOrder -> {
                        ConsumerPreOrderDto dto = new ConsumerPreOrderDto();
                        dto.setPreOrderID(consumerPreOrder.getPreOrderID());
                        dto.setProductName(consumerPreOrder.getProductName());
                        dto.setPrice(consumerPreOrder.getPrice());
                        dto.setRequiredQuantity(consumerPreOrder.getRequiredQuantity());
                        dto.setRequiredTime(consumerPreOrder.getRequiredTime());
                        dto.setAddedDate(consumerPreOrder.getAddedDate());
                        dto.setDescription(consumerPreOrder.getDescription());
                        dto.setActive(consumerPreOrder.isActive());
                        dto.setConfirmed(consumerPreOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(consumerPreOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerPreOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerPreOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerPreOrder.getUser().getFirstName());
                        userDto.setLastName(consumerPreOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerPreOrder.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerPreOrderGetResponse(consumerPreOrderDtoList);
            response.setStatus("200");
            response.setMessage("Order Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Order Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }
}
