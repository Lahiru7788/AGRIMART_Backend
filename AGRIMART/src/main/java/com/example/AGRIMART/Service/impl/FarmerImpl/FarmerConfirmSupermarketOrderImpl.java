package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmSupermarketOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmSupermarketOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerConfirmConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerConfirmSupermarketOrderRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketAddOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerConfirmSupermarketOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerConfirmSupermarketOrderImpl implements FarmerConfirmSupermarketOrderService {

    @Autowired
    private FarmerConfirmSupermarketOrderRepository farmerConfirmSupermarketOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupermarketAddOrderRepository supermarketAddOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerConfirmSupermarketOrderAddResponse saveOrUpdate(FarmerConfirmSupermarketOrderDto farmerConfirmSupermarketOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FarmerConfirmSupermarketOrderAddResponse response = new FarmerConfirmSupermarketOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SupermarketAddOrder> productOptionalSf = supermarketAddOrderRepository.findByOrderID(farmerConfirmSupermarketOrderDto.getOrderID());

        if (userOptional.isEmpty()) {
            FarmerConfirmSupermarketOrderAddResponse response = new FarmerConfirmSupermarketOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }


        if (productOptionalSf.isEmpty()) {
            FarmerConfirmSupermarketOrderAddResponse response = new FarmerConfirmSupermarketOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        SupermarketAddOrder supermarketAddOrder = productOptionalSf.get();

        // Check if there's an existing offer for this product
        Optional<FarmerConfirmSupermarketOrder> existingOrders = farmerConfirmSupermarketOrderRepository.findById(farmerConfirmSupermarketOrderDto.getOrderID());

        FarmerConfirmSupermarketOrder farmerConfirmSupermarketOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            farmerConfirmSupermarketOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            farmerConfirmSupermarketOrder = new FarmerConfirmSupermarketOrder();
            farmerConfirmSupermarketOrder.setUser(user);
            farmerConfirmSupermarketOrder.setSupermarketAddOrder(supermarketAddOrder);
            actionPerformed = "added";
        }

        // Update fields
        farmerConfirmSupermarketOrder.setProductName(farmerConfirmSupermarketOrderDto.getProductName());
        farmerConfirmSupermarketOrder.setPrice(farmerConfirmSupermarketOrderDto.getPrice());
        farmerConfirmSupermarketOrder.setDescription(farmerConfirmSupermarketOrderDto.getDescription());
        farmerConfirmSupermarketOrder.setRequiredQuantity(farmerConfirmSupermarketOrderDto.getRequiredQuantity());
        farmerConfirmSupermarketOrder.setRequiredTime(farmerConfirmSupermarketOrderDto.getRequiredTime());
        farmerConfirmSupermarketOrder.setAddedDate(farmerConfirmSupermarketOrderDto.getAddedDate());
        farmerConfirmSupermarketOrder.setActive(true);
        farmerConfirmSupermarketOrder.setUser(user);
        farmerConfirmSupermarketOrder.setSupermarketAddOrder(supermarketAddOrder);
        farmerConfirmSupermarketOrder.setPaid(false);

        FarmerConfirmSupermarketOrderAddResponse response = new FarmerConfirmSupermarketOrderAddResponse();
        try {
            // Save or update the offer
            FarmerConfirmSupermarketOrder savedOrder = farmerConfirmSupermarketOrderRepository.save(farmerConfirmSupermarketOrder);

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

    public FarmerConfirmSupermarketOrderGetResponse getFarmerConfirmSupermarketOrderByUserId(int userID) {
        FarmerConfirmSupermarketOrderGetResponse response = new FarmerConfirmSupermarketOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerConfirmSupermarketOrder> farmerConfirmSupermarketOrderList = farmerConfirmSupermarketOrderRepository.findByUser_UserID(userID);

            if (farmerConfirmSupermarketOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerConfirmSupermarketOrderDto> farmerConfirmSupermarketOrderDtoList = farmerConfirmSupermarketOrderList.stream()
                    .map(farmerConfirmSupermarketOrder -> {
                        FarmerConfirmSupermarketOrderDto dto = new FarmerConfirmSupermarketOrderDto();
                        dto.setOrderID(farmerConfirmSupermarketOrder.getConfirmOrderID());
                        dto.setProductName(farmerConfirmSupermarketOrder.getProductName());
                        dto.setPrice(farmerConfirmSupermarketOrder.getPrice());
                        dto.setRequiredQuantity(farmerConfirmSupermarketOrder.getRequiredQuantity());
                        dto.setRequiredTime(farmerConfirmSupermarketOrder.getRequiredTime());
                        dto.setAddedDate(farmerConfirmSupermarketOrder.getAddedDate());
                        dto.setDescription(farmerConfirmSupermarketOrder.getDescription());
                        dto.setActive(farmerConfirmSupermarketOrder.isActive());
                        dto.setPaid(farmerConfirmSupermarketOrder.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerConfirmSupermarketOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerConfirmSupermarketOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerConfirmSupermarketOrder.getUser().getFirstName());
                        userDto.setLastName(farmerConfirmSupermarketOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerConfirmSupermarketOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SupermarketAddOrderDto supermarketAddOrderDto = new SupermarketAddOrderDto();
                        supermarketAddOrderDto.setOrderID(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getOrderID());
                        supermarketAddOrderDto.setProductName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getProductName());
                        supermarketAddOrderDto.setPrice(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getPrice());
                        supermarketAddOrderDto.setAddedDate(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getAddedDate());
                        supermarketAddOrderDto.setDescription(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getDescription());
                        supermarketAddOrderDto.setRequiredQuantity(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredQuantity());
                        supermarketAddOrderDto.setRequiredTime(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredTime());
                        supermarketAddOrderDto.setActive(farmerConfirmSupermarketOrder.getSupermarketAddOrder().isActive());
                        supermarketAddOrderDto.setConfirmed(farmerConfirmSupermarketOrder.getSupermarketAddOrder().isConfirmed());
                        supermarketAddOrderDto.setRequiredQuantity(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredQuantity());

                        UserDto supermarketUserDto = new UserDto();
                        supermarketUserDto.setUserID(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserID());
                        supermarketUserDto.setUserEmail(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserEmail());
                        supermarketUserDto.setFirstName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getFirstName());
                        supermarketUserDto.setLastName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getLastName());
                        supermarketUserDto.setUserType(String.valueOf(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserType()));
                        supermarketAddOrderDto.setUser(supermarketUserDto);

                        dto.setSupermarketAddOrder(supermarketAddOrderDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerConfirmSupermarketOrderGetResponse(farmerConfirmSupermarketOrderDtoList);
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


    public FarmerConfirmSupermarketOrderGetResponse getFarmerConfirmSupermarketOrderBySupermarketUserId(int supermarketUserID) {
        FarmerConfirmSupermarketOrderGetResponse response = new FarmerConfirmSupermarketOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<FarmerConfirmSupermarketOrder> farmerConfirmSupermarketOrderList = farmerConfirmSupermarketOrderRepository.findBySupermarketAddOrder_User_UserID(supermarketUserID);

            if (farmerConfirmSupermarketOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + supermarketUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<FarmerConfirmSupermarketOrderDto> farmerConfirmSupermarketOrderDtoList = farmerConfirmSupermarketOrderList.stream()
                    .map(farmerConfirmSupermarketOrder -> {
                        FarmerConfirmSupermarketOrderDto dto = new FarmerConfirmSupermarketOrderDto();
                        dto.setOrderID(farmerConfirmSupermarketOrder.getConfirmOrderID());
                        dto.setProductName(farmerConfirmSupermarketOrder.getProductName());
                        dto.setPrice(farmerConfirmSupermarketOrder.getPrice());
                        dto.setRequiredQuantity(farmerConfirmSupermarketOrder.getRequiredQuantity());
                        dto.setRequiredTime(farmerConfirmSupermarketOrder.getRequiredTime());
                        dto.setAddedDate(farmerConfirmSupermarketOrder.getAddedDate());
                        dto.setDescription(farmerConfirmSupermarketOrder.getDescription());
                        dto.setActive(farmerConfirmSupermarketOrder.isActive());
                        dto.setPaid(farmerConfirmSupermarketOrder.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerConfirmSupermarketOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerConfirmSupermarketOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerConfirmSupermarketOrder.getUser().getFirstName());
                        userDto.setLastName(farmerConfirmSupermarketOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerConfirmSupermarketOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SupermarketAddOrderDto supermarketAddOrderDto = new SupermarketAddOrderDto();
                        supermarketAddOrderDto.setOrderID(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getOrderID());
                        supermarketAddOrderDto.setProductName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getProductName());
                        supermarketAddOrderDto.setPrice(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getPrice());
                        supermarketAddOrderDto.setAddedDate(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getAddedDate());
                        supermarketAddOrderDto.setDescription(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getDescription());
                        supermarketAddOrderDto.setRequiredQuantity(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredQuantity());
                        supermarketAddOrderDto.setRequiredTime(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredTime());
                        supermarketAddOrderDto.setActive(farmerConfirmSupermarketOrder.getSupermarketAddOrder().isActive());
                        supermarketAddOrderDto.setConfirmed(farmerConfirmSupermarketOrder.getSupermarketAddOrder().isConfirmed());
                        supermarketAddOrderDto.setRequiredQuantity(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredQuantity());

                        UserDto supermarketUserDto = new UserDto();
                        supermarketUserDto.setUserID(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserID());
                        supermarketUserDto.setUserEmail(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserEmail());
                        supermarketUserDto.setFirstName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getFirstName());
                        supermarketUserDto.setLastName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getLastName());
                        supermarketUserDto.setUserType(String.valueOf(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserType()));
                        supermarketAddOrderDto.setUser(supermarketUserDto);

                        dto.setSupermarketAddOrder(supermarketAddOrderDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerConfirmSupermarketOrderGetResponse(farmerConfirmSupermarketOrderDtoList);
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

    public FarmerConfirmSupermarketOrderGetResponse getFarmerConfirmSupermarketOrderByOrderId(int orderID) {
        FarmerConfirmSupermarketOrderGetResponse response = new FarmerConfirmSupermarketOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerConfirmSupermarketOrder> farmerConfirmSupermarketOrderList = farmerConfirmSupermarketOrderRepository.findBySupermarketAddOrder_OrderID(orderID);

            if (farmerConfirmSupermarketOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + orderID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerConfirmSupermarketOrderDto> farmerConfirmSupermarketOrderDtoList = farmerConfirmSupermarketOrderList.stream()
                    .map(farmerConfirmSupermarketOrder -> {
                        FarmerConfirmSupermarketOrderDto dto = new FarmerConfirmSupermarketOrderDto();
                        dto.setOrderID(farmerConfirmSupermarketOrder.getConfirmOrderID());
                        dto.setProductName(farmerConfirmSupermarketOrder.getProductName());
                        dto.setPrice(farmerConfirmSupermarketOrder.getPrice());
                        dto.setRequiredQuantity(farmerConfirmSupermarketOrder.getRequiredQuantity());
                        dto.setRequiredTime(farmerConfirmSupermarketOrder.getRequiredTime());
                        dto.setAddedDate(farmerConfirmSupermarketOrder.getAddedDate());
                        dto.setDescription(farmerConfirmSupermarketOrder.getDescription());
                        dto.setActive(farmerConfirmSupermarketOrder.isActive());
                        dto.setPaid(farmerConfirmSupermarketOrder.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerConfirmSupermarketOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerConfirmSupermarketOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerConfirmSupermarketOrder.getUser().getFirstName());
                        userDto.setLastName(farmerConfirmSupermarketOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerConfirmSupermarketOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SupermarketAddOrderDto supermarketAddOrderDto = new SupermarketAddOrderDto();
                        supermarketAddOrderDto.setOrderID(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getOrderID());
                        supermarketAddOrderDto.setProductName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getProductName());
                        supermarketAddOrderDto.setPrice(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getPrice());
                        supermarketAddOrderDto.setAddedDate(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getAddedDate());
                        supermarketAddOrderDto.setDescription(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getDescription());
                        supermarketAddOrderDto.setRequiredQuantity(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredQuantity());
                        supermarketAddOrderDto.setRequiredTime(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredTime());
                        supermarketAddOrderDto.setActive(farmerConfirmSupermarketOrder.getSupermarketAddOrder().isActive());
                        supermarketAddOrderDto.setConfirmed(farmerConfirmSupermarketOrder.getSupermarketAddOrder().isConfirmed());
                        supermarketAddOrderDto.setRequiredQuantity(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getRequiredQuantity());

                        UserDto supermarketUserDto = new UserDto();
                        supermarketUserDto.setUserID(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserID());
                        supermarketUserDto.setUserEmail(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserEmail());
                        supermarketUserDto.setFirstName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getFirstName());
                        supermarketUserDto.setLastName(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getLastName());
                        supermarketUserDto.setUserType(String.valueOf(farmerConfirmSupermarketOrder.getSupermarketAddOrder().getUser().getUserType()));
                        supermarketAddOrderDto.setUser(supermarketUserDto);

                        dto.setSupermarketAddOrder(supermarketAddOrderDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerConfirmSupermarketOrderGetResponse(farmerConfirmSupermarketOrderDtoList);
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
    public FarmerConfirmSupermarketOrderDeleteResponse DeleteFarmerConfirmSupermarketOrderResponse(int orderID) {
        FarmerConfirmSupermarketOrderDeleteResponse response = new FarmerConfirmSupermarketOrderDeleteResponse();

        //calculation part
        FarmerConfirmSupermarketOrder farmerConfirmSupermarketOrder;
        farmerConfirmSupermarketOrder = farmerConfirmSupermarketOrderRepository.findByConfirmOrderID(orderID);



        try {
            farmerConfirmSupermarketOrder.setActive(false);
            farmerConfirmSupermarketOrderRepository.save(farmerConfirmSupermarketOrder);
            response.setFarmerConfirmSupermarketOrderDeleteResponse(farmerConfirmSupermarketOrder);
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
    public FarmerConfirmSupermarketOrderPaymentResponse PaymentFarmerConfirmSupermarketOrderResponse(int orderID) {
        FarmerConfirmSupermarketOrderPaymentResponse response = new FarmerConfirmSupermarketOrderPaymentResponse();

        //calculation part
        FarmerConfirmSupermarketOrder farmerConfirmSupermarketOrder;
        farmerConfirmSupermarketOrder = farmerConfirmSupermarketOrderRepository.findByConfirmOrderID(orderID);



        try {
            farmerConfirmSupermarketOrder.setPaid(true);
            farmerConfirmSupermarketOrderRepository.save(farmerConfirmSupermarketOrder);
            response.setFarmerConfirmSupermarketOrderPaymentResponse(farmerConfirmSupermarketOrder);
            response.setMessage("order Id : " + orderID + " item delete successfully");
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
