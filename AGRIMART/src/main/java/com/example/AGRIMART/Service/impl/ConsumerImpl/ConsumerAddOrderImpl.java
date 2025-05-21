package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerAddOrderGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerAddOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerAddOrderImpl implements ConsumerAddOrderService {

    @Autowired
    private ConsumerAddOrderRepository consumerAddOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
//    public FarmerProductAddResponse save(FarmerProductDto farmerProductDto) {
//
//        String username = (String) session.getAttribute("userEmail");
//
//        if (username == null || username.isEmpty()) {
//            FarmerProductAddResponse response = new FarmerProductAddResponse();
//            response.setMessage("User is not logged in or session expired.");
//            response.setStatus("401"); // Unauthorized
//            return response;
//        }
//
//        Optional<User> userOptional = userRepository.findByUserEmail(username);
//
//        if (userOptional.isEmpty()) {
//            FarmerProductAddResponse response = new FarmerProductAddResponse();
//            response.setMessage("User not found for the given username.");
//            return response;
//        }
//
//        User user = userOptional.get();
//        FarmerProduct farmerProduct = new FarmerProduct();
//        farmerProduct.setProductName(farmerProductDto.getProductName());
//        farmerProduct.setProductImage(farmerProductDto.getProductImage());
//        farmerProduct.setPrice(farmerProductDto.getPrice());
//        farmerProduct.setDescription(farmerProductDto.getDescription());
//        farmerProduct.setAvailableQuantity(farmerProductDto.getAvailableQuantity());
//        farmerProduct.setMinimumQuantity(farmerProductDto.getMinimumQuantity());
//        farmerProduct.setAddedDate(farmerProductDto.getAddedDate());
//        farmerProduct.setProductCategory(FarmerProductDto.ProductCategory.valueOf(farmerProductDto.getProductCategory()));
//
//        farmerProduct.setUser(user);
//        farmerProduct.setActive(true);
//        farmerProduct.setDeleted(false);
//        farmerProduct.setQuantityLowered(false);
//
//        FarmerProductAddResponse response = new FarmerProductAddResponse();
//        try {
//            FarmerProduct saveFarmerProduct = farmerProductRepository.save(farmerProduct);
//            if (saveFarmerProduct != null) {
//                response.setMessage("Product was added successfully.");
//                response.setStatus("200");
//                response.setResponseCode("1000");
//            } else {
//                response.setMessage("Failed to add product.");
//                response.setStatus("400");
//            }
//        } catch (Exception e) {
//            response.setMessage("Error: " + e.getMessage());
//            response.setStatus("500"); // Internal server error
//        }
//
//        return response;
//    }

    public ConsumerAddOrderAddResponse saveOrUpdate(ConsumerAddOrderDto consumerAddOrderDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            ConsumerAddOrderAddResponse response = new ConsumerAddOrderAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            ConsumerAddOrderAddResponse response = new ConsumerAddOrderAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<ConsumerAddOrder> existingProductOptional = consumerAddOrderRepository.findById(consumerAddOrderDto.getOrderID());

        ConsumerAddOrder consumerAddOrder;
        String actionPerformed;

        if (!existingProductOptional.isEmpty()) {
            consumerAddOrder = existingProductOptional.get();
            actionPerformed = "updated";
        } else {
            consumerAddOrder = new ConsumerAddOrder();
            consumerAddOrder.setUser(user);

            actionPerformed = "added";
        }
        // If product exists, we update the fields; if not, we create a new one
        consumerAddOrder.setProductName(consumerAddOrderDto.getProductName());
        consumerAddOrder.setPrice(consumerAddOrderDto.getPrice());
        consumerAddOrder.setDescription(consumerAddOrderDto.getDescription());
        consumerAddOrder.setRequiredQuantity(consumerAddOrderDto.getRequiredQuantity());
        consumerAddOrder.setRequiredTime(consumerAddOrderDto.getRequiredTime());
        consumerAddOrder.setAddedDate(consumerAddOrderDto.getAddedDate());
        consumerAddOrder.setProductCategory(ConsumerAddOrderDto.ProductCategory.valueOf(consumerAddOrderDto.getProductCategory()));

        // Ensure the user is assigned
        consumerAddOrder.setUser(user);

        // Set the flags based on business rules
        consumerAddOrder.setActive(true);
        consumerAddOrder.setConfirmed(false);


        ConsumerAddOrderAddResponse response = new ConsumerAddOrderAddResponse();
        try {
            // Save or update the product
            ConsumerAddOrder savedAddOrder = consumerAddOrderRepository.save(consumerAddOrder);

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
    public ConsumerAddOrderGetResponse GetAllConsumerOrders() {
        ConsumerAddOrderGetResponse response = new ConsumerAddOrderGetResponse();
        try {
            // Fetch all user details
            List<ConsumerAddOrder> consumerAddOrderList = consumerAddOrderRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<ConsumerAddOrderDto> consumerAddOrderDtoList = consumerAddOrderList.stream()
                    .map(consumerAddOrder -> {
                        ConsumerAddOrderDto dto = new ConsumerAddOrderDto();
                        dto.setOrderID(consumerAddOrder.getOrderID());
                        dto.setProductName(consumerAddOrder.getProductName());
                        dto.setPrice(consumerAddOrder.getPrice());
                        dto.setRequiredQuantity(consumerAddOrder.getRequiredQuantity());
                        dto.setRequiredTime(consumerAddOrder.getRequiredTime());
                        dto.setAddedDate(consumerAddOrder.getAddedDate());
                        dto.setDescription(consumerAddOrder.getDescription());
                        dto.setActive(consumerAddOrder.isActive());
                        dto.setConfirmed(consumerAddOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(consumerAddOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerAddOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerAddOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerAddOrder.getUser().getFirstName());
                        userDto.setLastName(consumerAddOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerAddOrder.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerAddOrderGetResponse(consumerAddOrderDtoList);
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

    public ConsumerAddOrderGetResponse getConsumerAddOrderByUserId(int userID) {
        ConsumerAddOrderGetResponse response = new ConsumerAddOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerAddOrder> consumerAddOrderList = consumerAddOrderRepository.findByUser_UserID(userID);

            if (consumerAddOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerAddOrderDto> consumerAddOrderDtoList = consumerAddOrderList.stream()
                    .map(consumerAddOrder -> {
                        ConsumerAddOrderDto dto = new ConsumerAddOrderDto();
                        dto.setOrderID(consumerAddOrder.getOrderID());
                        dto.setProductName(consumerAddOrder.getProductName());
                        dto.setPrice(consumerAddOrder.getPrice());
                        dto.setRequiredQuantity(consumerAddOrder.getRequiredQuantity());
                        dto.setRequiredTime(consumerAddOrder.getRequiredTime());
                        dto.setAddedDate(consumerAddOrder.getAddedDate());
                        dto.setDescription(consumerAddOrder.getDescription());
                        dto.setActive(consumerAddOrder.isActive());
                        dto.setConfirmed(consumerAddOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(consumerAddOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerAddOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerAddOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerAddOrder.getUser().getFirstName());
                        userDto.setLastName(consumerAddOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerAddOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerAddOrderGetResponse(consumerAddOrderDtoList);
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
    public ConsumerAddOrderDeleteResponse DeleteConsumerResponse(int orderID) {
        ConsumerAddOrderDeleteResponse response = new ConsumerAddOrderDeleteResponse();

        //calculation part
        ConsumerAddOrder consumerAddOrder;
        consumerAddOrder = consumerAddOrderRepository.findByOrderID(orderID);



        try {
            consumerAddOrder.setActive(false);
            consumerAddOrderRepository.save(consumerAddOrder);
            response.setConsumerAddOrderDeleteResponse(consumerAddOrder);
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
