package com.example.AGRIMART.Service.impl.SupermarketImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketAddOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketAddOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupermarketAddOrderImpl implements SupermarketAddOrderService {

    @Autowired
    private SupermarketAddOrderRepository supermarketAddOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SupermarketAddOrderAddResponse saveOrUpdate(SupermarketAddOrderDto supermarketAddOrderDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            SupermarketAddOrderAddResponse response = new SupermarketAddOrderAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            SupermarketAddOrderAddResponse response = new SupermarketAddOrderAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<SupermarketAddOrder> existingProductOptional = supermarketAddOrderRepository.findById(supermarketAddOrderDto.getOrderID());

        SupermarketAddOrder supermarketAddOrder;
        String actionPerformed;

        if (!existingProductOptional.isEmpty()) {
            supermarketAddOrder = existingProductOptional.get();
            actionPerformed = "updated";
        } else {
            supermarketAddOrder = new SupermarketAddOrder();
            supermarketAddOrder.setUser(user);

            actionPerformed = "added";
        }
        // If product exists, we update the fields; if not, we create a new one
        supermarketAddOrder.setProductName(supermarketAddOrderDto.getProductName());
        supermarketAddOrder.setPrice(supermarketAddOrderDto.getPrice());
        supermarketAddOrder.setDescription(supermarketAddOrderDto.getDescription());
        supermarketAddOrder.setRequiredQuantity(supermarketAddOrderDto.getRequiredQuantity());
        supermarketAddOrder.setRequiredTime(supermarketAddOrderDto.getRequiredTime());
        supermarketAddOrder.setAddedDate(supermarketAddOrderDto.getAddedDate());
        supermarketAddOrder.setProductCategory(ConsumerAddOrderDto.ProductCategory.valueOf(supermarketAddOrderDto.getProductCategory()));

        // Ensure the user is assigned
        supermarketAddOrder.setUser(user);

        // Set the flags based on business rules
        supermarketAddOrder.setActive(true);
        supermarketAddOrder.setConfirmed(false);


        SupermarketAddOrderAddResponse response = new SupermarketAddOrderAddResponse();
        try {
            // Save or update the product
            SupermarketAddOrder savedAddOrder = supermarketAddOrderRepository.save(supermarketAddOrder);

            if (savedAddOrder != null) {
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

    public SupermarketAddOrderGetResponse GetAllSupermarketOrders() {
        SupermarketAddOrderGetResponse response = new SupermarketAddOrderGetResponse();
        try {
            // Fetch all user details
            List<SupermarketAddOrder> supermarketAddOrderList = supermarketAddOrderRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<SupermarketAddOrderDto> supermarketAddOrderDtoList = supermarketAddOrderList.stream()
                    .map(supermarketAddOrder -> {
                        SupermarketAddOrderDto dto = new SupermarketAddOrderDto();
                        dto.setOrderID(supermarketAddOrder.getOrderID());
                        dto.setProductName(supermarketAddOrder.getProductName());
                        dto.setPrice(supermarketAddOrder.getPrice());
                        dto.setRequiredQuantity(supermarketAddOrder.getRequiredQuantity());
                        dto.setRequiredTime(supermarketAddOrder.getRequiredTime());
                        dto.setAddedDate(supermarketAddOrder.getAddedDate());
                        dto.setDescription(supermarketAddOrder.getDescription());
                        dto.setActive(supermarketAddOrder.isActive());
                        dto.setConfirmed(supermarketAddOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketAddOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketAddOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketAddOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketAddOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketAddOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketAddOrder.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketAddOrderGetResponse(supermarketAddOrderDtoList);
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
    

    public SupermarketAddOrderGetResponse getSupermarketAddOrderByUserId(int userID) {
        SupermarketAddOrderGetResponse response = new SupermarketAddOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SupermarketAddOrder> supermarketAddOrderList = supermarketAddOrderRepository.findByUser_UserID(userID);

            if (supermarketAddOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SupermarketAddOrderDto> supermarketAddOrderDtoList = supermarketAddOrderList.stream()
                    .map(supermarketAddOrder -> {
                        SupermarketAddOrderDto dto = new SupermarketAddOrderDto();
                        dto.setOrderID(supermarketAddOrder.getOrderID());
                        dto.setProductName(supermarketAddOrder.getProductName());
                        dto.setPrice(supermarketAddOrder.getPrice());
                        dto.setRequiredQuantity(supermarketAddOrder.getRequiredQuantity());
                        dto.setRequiredTime(supermarketAddOrder.getRequiredTime());
                        dto.setAddedDate(supermarketAddOrder.getAddedDate());
                        dto.setDescription(supermarketAddOrder.getDescription());
                        dto.setActive(supermarketAddOrder.isActive());
                        dto.setConfirmed(supermarketAddOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketAddOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketAddOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketAddOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketAddOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketAddOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketAddOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketAddOrderGetResponse(supermarketAddOrderDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public SupermarketAddOrderDeleteResponse DeleteSupermarketResponse(int orderID) {
        SupermarketAddOrderDeleteResponse response = new SupermarketAddOrderDeleteResponse();

        //calculation part
        SupermarketAddOrder supermarketAddOrder;
        supermarketAddOrder = supermarketAddOrderRepository.findByOrderID(orderID);



        try {
            supermarketAddOrder.setActive(false);
            supermarketAddOrderRepository.save(supermarketAddOrder);
            response.setSupermarketAddOrderDeleteResponse(supermarketAddOrder);
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
