package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerConfirmConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerSeedsOrderRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerConfirmConsumerOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerConfirmConsumerOrderImpl implements FarmerConfirmConsumerOrderService {

    @Autowired
    private FarmerConfirmConsumerOrderRepository farmerConfirmConsumerOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsumerAddOrderRepository consumerAddOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerConfirmConsumerOrderAddResponse saveOrUpdate(FarmerConfirmConsumerOrderDto farmerConfirmConsumerOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FarmerConfirmConsumerOrderAddResponse response = new FarmerConfirmConsumerOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<ConsumerAddOrder> productOptionalSf = consumerAddOrderRepository.findByOrderID(farmerConfirmConsumerOrderDto.getOrderID());

        if (userOptional.isEmpty()) {
            FarmerConfirmConsumerOrderAddResponse response = new FarmerConfirmConsumerOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }


        if (productOptionalSf.isEmpty()) {
            FarmerConfirmConsumerOrderAddResponse response = new FarmerConfirmConsumerOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        ConsumerAddOrder consumerAddOrder = productOptionalSf.get();

        // Check if there's an existing offer for this product
        Optional<FarmerConfirmConsumerOrder> existingOrders = farmerConfirmConsumerOrderRepository.findById(farmerConfirmConsumerOrderDto.getOrderID());

        FarmerConfirmConsumerOrder farmerConfirmConsumerOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            farmerConfirmConsumerOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            farmerConfirmConsumerOrder = new FarmerConfirmConsumerOrder();
            farmerConfirmConsumerOrder.setUser(user);
            farmerConfirmConsumerOrder.setConsumerAddOrder(consumerAddOrder);
            actionPerformed = "added";
        }

        // Update fields
        farmerConfirmConsumerOrder.setProductName(farmerConfirmConsumerOrderDto.getProductName());
        farmerConfirmConsumerOrder.setPrice(farmerConfirmConsumerOrderDto.getPrice());
        farmerConfirmConsumerOrder.setDescription(farmerConfirmConsumerOrderDto.getDescription());
        farmerConfirmConsumerOrder.setRequiredQuantity(farmerConfirmConsumerOrderDto.getRequiredQuantity());
        farmerConfirmConsumerOrder.setRequiredTime(farmerConfirmConsumerOrderDto.getRequiredTime());
        farmerConfirmConsumerOrder.setAddedDate(farmerConfirmConsumerOrderDto.getAddedDate());
        farmerConfirmConsumerOrder.setActive(true);
        farmerConfirmConsumerOrder.setUser(user);
        farmerConfirmConsumerOrder.setConsumerAddOrder(consumerAddOrder);
        farmerConfirmConsumerOrder.setPaid(false);

        FarmerConfirmConsumerOrderAddResponse response = new FarmerConfirmConsumerOrderAddResponse();
        try {
            // Save or update the offer
            FarmerConfirmConsumerOrder savedOrder = farmerConfirmConsumerOrderRepository.save(farmerConfirmConsumerOrder);

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

    public FarmerConfirmConsumerOrderGetResponse getFarmerConfirmConsumerOrderByUserId(int userID) {
        FarmerConfirmConsumerOrderGetResponse response = new FarmerConfirmConsumerOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerConfirmConsumerOrder> farmerConfirmConsumerOrderList = farmerConfirmConsumerOrderRepository.findByUser_UserID(userID);

            if (farmerConfirmConsumerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerConfirmConsumerOrderDto> farmerConfirmConsumerOrderDtoList = farmerConfirmConsumerOrderList.stream()
                    .map(farmerConfirmConsumerOrder -> {
                        FarmerConfirmConsumerOrderDto dto = new FarmerConfirmConsumerOrderDto();
                        dto.setOrderID(farmerConfirmConsumerOrder.getConfirmOrderID());
                        dto.setProductName(farmerConfirmConsumerOrder.getProductName());
                        dto.setPrice(farmerConfirmConsumerOrder.getPrice());
                        dto.setRequiredQuantity(farmerConfirmConsumerOrder.getRequiredQuantity());
                        dto.setRequiredTime(farmerConfirmConsumerOrder.getRequiredTime());
                        dto.setAddedDate(farmerConfirmConsumerOrder.getAddedDate());
                        dto.setDescription(farmerConfirmConsumerOrder.getDescription());
                        dto.setActive(farmerConfirmConsumerOrder.isActive());
                        dto.setPaid(farmerConfirmConsumerOrder.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerConfirmConsumerOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerConfirmConsumerOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerConfirmConsumerOrder.getUser().getFirstName());
                        userDto.setLastName(farmerConfirmConsumerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerConfirmConsumerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        ConsumerAddOrderDto consumerAddOrderDto = new ConsumerAddOrderDto();
                        consumerAddOrderDto.setOrderID(farmerConfirmConsumerOrder.getConsumerAddOrder().getOrderID());
                        consumerAddOrderDto.setProductName(farmerConfirmConsumerOrder.getConsumerAddOrder().getProductName());
                        consumerAddOrderDto.setPrice(farmerConfirmConsumerOrder.getConsumerAddOrder().getPrice());
                        consumerAddOrderDto.setAddedDate(farmerConfirmConsumerOrder.getConsumerAddOrder().getAddedDate());
                        consumerAddOrderDto.setDescription(farmerConfirmConsumerOrder.getConsumerAddOrder().getDescription());
                        consumerAddOrderDto.setRequiredQuantity(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredQuantity());
                        consumerAddOrderDto.setRequiredTime(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredTime());
                        consumerAddOrderDto.setActive(farmerConfirmConsumerOrder.getConsumerAddOrder().isActive());
                        consumerAddOrderDto.setConfirmed(farmerConfirmConsumerOrder.getConsumerAddOrder().isConfirmed());
                        consumerAddOrderDto.setRequiredQuantity(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredQuantity());

                        UserDto consumerUserDto = new UserDto();
                        consumerUserDto.setUserID(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserID());
                        consumerUserDto.setUserEmail(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserEmail());
                        consumerUserDto.setFirstName(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getFirstName());
                        consumerUserDto.setLastName(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getLastName());
                        consumerUserDto.setUserType(String.valueOf(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserType()));
                        consumerAddOrderDto.setUser(consumerUserDto);

                        dto.setConsumerAddOrder(consumerAddOrderDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerConfirmConsumerOrderGetResponse(farmerConfirmConsumerOrderDtoList);
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


    public FarmerConfirmConsumerOrderGetResponse getFarmerConfirmConsumerOrderByConsumerUserId(int consumerUserID) {
        FarmerConfirmConsumerOrderGetResponse response = new FarmerConfirmConsumerOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<FarmerConfirmConsumerOrder> farmerConfirmConsumerOrderList = farmerConfirmConsumerOrderRepository.findByConsumerAddOrder_User_UserID(consumerUserID);

            if (farmerConfirmConsumerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + consumerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<FarmerConfirmConsumerOrderDto> farmerConfirmConsumerOrderDtoList = farmerConfirmConsumerOrderList.stream()
                    .map(farmerConfirmConsumerOrder -> {
                        FarmerConfirmConsumerOrderDto dto = new FarmerConfirmConsumerOrderDto();
                        dto.setOrderID(farmerConfirmConsumerOrder.getConfirmOrderID());
                        dto.setProductName(farmerConfirmConsumerOrder.getProductName());
                        dto.setPrice(farmerConfirmConsumerOrder.getPrice());
                        dto.setRequiredQuantity(farmerConfirmConsumerOrder.getRequiredQuantity());
                        dto.setRequiredTime(farmerConfirmConsumerOrder.getRequiredTime());
                        dto.setAddedDate(farmerConfirmConsumerOrder.getAddedDate());
                        dto.setDescription(farmerConfirmConsumerOrder.getDescription());
                        dto.setActive(farmerConfirmConsumerOrder.isActive());
                        dto.setPaid(farmerConfirmConsumerOrder.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerConfirmConsumerOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerConfirmConsumerOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerConfirmConsumerOrder.getUser().getFirstName());
                        userDto.setLastName(farmerConfirmConsumerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerConfirmConsumerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        ConsumerAddOrderDto consumerAddOrderDto = new ConsumerAddOrderDto();
                        consumerAddOrderDto.setOrderID(farmerConfirmConsumerOrder.getConsumerAddOrder().getOrderID());
                        consumerAddOrderDto.setProductName(farmerConfirmConsumerOrder.getConsumerAddOrder().getProductName());
                        consumerAddOrderDto.setPrice(farmerConfirmConsumerOrder.getConsumerAddOrder().getPrice());
                        consumerAddOrderDto.setAddedDate(farmerConfirmConsumerOrder.getConsumerAddOrder().getAddedDate());
                        consumerAddOrderDto.setDescription(farmerConfirmConsumerOrder.getConsumerAddOrder().getDescription());
                        consumerAddOrderDto.setRequiredQuantity(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredQuantity());
                        consumerAddOrderDto.setRequiredTime(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredTime());
                        consumerAddOrderDto.setActive(farmerConfirmConsumerOrder.getConsumerAddOrder().isActive());
                        consumerAddOrderDto.setConfirmed(farmerConfirmConsumerOrder.getConsumerAddOrder().isConfirmed());
                        consumerAddOrderDto.setRequiredQuantity(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredQuantity());

                        UserDto consumerUserDto = new UserDto();
                        consumerUserDto.setUserID(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserID());
                        consumerUserDto.setUserEmail(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserEmail());
                        consumerUserDto.setFirstName(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getFirstName());
                        consumerUserDto.setLastName(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getLastName());
                        consumerUserDto.setUserType(String.valueOf(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserType()));
                        consumerAddOrderDto.setUser(consumerUserDto);

                        dto.setConsumerAddOrder(consumerAddOrderDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerConfirmConsumerOrderGetResponse(farmerConfirmConsumerOrderDtoList);
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

    public FarmerConfirmConsumerOrderGetResponse getFarmerConfirmConsumerOrderByOrderId(int orderID) {
        FarmerConfirmConsumerOrderGetResponse response = new FarmerConfirmConsumerOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerConfirmConsumerOrder> farmerConfirmConsumerOrderList = farmerConfirmConsumerOrderRepository.findByConsumerAddOrder_OrderID(orderID);

            if (farmerConfirmConsumerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + orderID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerConfirmConsumerOrderDto> farmerConfirmConsumerOrderDtoList = farmerConfirmConsumerOrderList.stream()
                    .map(farmerConfirmConsumerOrder -> {
                        FarmerConfirmConsumerOrderDto dto = new FarmerConfirmConsumerOrderDto();
                        dto.setOrderID(farmerConfirmConsumerOrder.getConfirmOrderID());
                        dto.setProductName(farmerConfirmConsumerOrder.getProductName());
                        dto.setPrice(farmerConfirmConsumerOrder.getPrice());
                        dto.setRequiredQuantity(farmerConfirmConsumerOrder.getRequiredQuantity());
                        dto.setRequiredTime(farmerConfirmConsumerOrder.getRequiredTime());
                        dto.setAddedDate(farmerConfirmConsumerOrder.getAddedDate());
                        dto.setDescription(farmerConfirmConsumerOrder.getDescription());
                        dto.setActive(farmerConfirmConsumerOrder.isActive());
                        dto.setPaid(farmerConfirmConsumerOrder.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerConfirmConsumerOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerConfirmConsumerOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerConfirmConsumerOrder.getUser().getFirstName());
                        userDto.setLastName(farmerConfirmConsumerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerConfirmConsumerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        ConsumerAddOrderDto consumerAddOrderDto = new ConsumerAddOrderDto();
                        consumerAddOrderDto.setOrderID(farmerConfirmConsumerOrder.getConsumerAddOrder().getOrderID());
                        consumerAddOrderDto.setProductName(farmerConfirmConsumerOrder.getConsumerAddOrder().getProductName());
                        consumerAddOrderDto.setPrice(farmerConfirmConsumerOrder.getConsumerAddOrder().getPrice());
                        consumerAddOrderDto.setAddedDate(farmerConfirmConsumerOrder.getConsumerAddOrder().getAddedDate());
                        consumerAddOrderDto.setDescription(farmerConfirmConsumerOrder.getConsumerAddOrder().getDescription());
                        consumerAddOrderDto.setRequiredQuantity(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredQuantity());
                        consumerAddOrderDto.setRequiredTime(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredTime());
                        consumerAddOrderDto.setActive(farmerConfirmConsumerOrder.getConsumerAddOrder().isActive());
                        consumerAddOrderDto.setConfirmed(farmerConfirmConsumerOrder.getConsumerAddOrder().isConfirmed());
                        consumerAddOrderDto.setRequiredQuantity(farmerConfirmConsumerOrder.getConsumerAddOrder().getRequiredQuantity());

                        UserDto consumerUserDto = new UserDto();
                        consumerUserDto.setUserID(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserID());
                        consumerUserDto.setUserEmail(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserEmail());
                        consumerUserDto.setFirstName(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getFirstName());
                        consumerUserDto.setLastName(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getLastName());
                        consumerUserDto.setUserType(String.valueOf(farmerConfirmConsumerOrder.getConsumerAddOrder().getUser().getUserType()));
                        consumerAddOrderDto.setUser(consumerUserDto);

                        dto.setConsumerAddOrder(consumerAddOrderDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerConfirmConsumerOrderGetResponse(farmerConfirmConsumerOrderDtoList);
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
    public FarmerConfirmConsumerOrderDeleteResponse DeleteFarmerConfirmConsumerOrderResponse(int orderID) {
        FarmerConfirmConsumerOrderDeleteResponse response = new FarmerConfirmConsumerOrderDeleteResponse();

        //calculation part
        FarmerConfirmConsumerOrder farmerConfirmConsumerOrder;
        farmerConfirmConsumerOrder = farmerConfirmConsumerOrderRepository.findByConfirmOrderID(orderID);



        try {
            farmerConfirmConsumerOrder.setActive(false);
            farmerConfirmConsumerOrderRepository.save(farmerConfirmConsumerOrder);
            response.setFarmerConfirmConsumerOrderDeleteResponse(farmerConfirmConsumerOrder);
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
    public FarmerConfirmConsumerOrderPaymentResponse PaymentFarmerConfirmConsumerOrderResponse(int orderID) {
        FarmerConfirmConsumerOrderPaymentResponse response = new FarmerConfirmConsumerOrderPaymentResponse();

        //calculation part
        FarmerConfirmConsumerOrder farmerConfirmConsumerOrder;
        farmerConfirmConsumerOrder = farmerConfirmConsumerOrderRepository.findByConfirmOrderID(orderID);



        try {
            farmerConfirmConsumerOrder.setPaid(true);
            farmerConfirmConsumerOrderRepository.save(farmerConfirmConsumerOrder);
            response.setFarmerConfirmConsumerOrderPaymentResponse(farmerConfirmConsumerOrder);
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
