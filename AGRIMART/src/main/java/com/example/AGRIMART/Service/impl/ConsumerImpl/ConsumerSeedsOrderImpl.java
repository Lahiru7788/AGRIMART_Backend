package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import com.example.AGRIMART.Service.ConsumerService.ConsumerSeedsOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerSeedsOrderImpl implements ConsumerSeedsOrderService {

    @Autowired
    private ConsumerSeedsOrderRepository consumerSeedsOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SFProductRepository sfProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerSeedsOrderAddResponse saveOrUpdate(ConsumerSeedsOrderDto consumerSeedsOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            ConsumerSeedsOrderAddResponse response = new ConsumerSeedsOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SFProduct> productOptionalSf = sfProductRepository.findByProductName(productID);

        if (userOptional.isEmpty()) {
            ConsumerSeedsOrderAddResponse response = new ConsumerSeedsOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }


        if (productOptionalSf.isEmpty()) {
            ConsumerSeedsOrderAddResponse response = new ConsumerSeedsOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        SFProduct sfProduct = productOptionalSf.get();

        // Check if there's an existing offer for this product
        Optional<ConsumerSeedsOrder> existingOrders = consumerSeedsOrderRepository.findById(consumerSeedsOrderDto.getOrderID());

        ConsumerSeedsOrder consumerSeedsOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            consumerSeedsOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            consumerSeedsOrder = new ConsumerSeedsOrder();
            consumerSeedsOrder.setUser(user);
            consumerSeedsOrder.setSFProduct(sfProduct);
            actionPerformed = "added";
        }

        // Update fields
        consumerSeedsOrder.setProductName(consumerSeedsOrderDto.getProductName());
        consumerSeedsOrder.setPrice(consumerSeedsOrderDto.getPrice());
        consumerSeedsOrder.setDescription(consumerSeedsOrderDto.getDescription());
        consumerSeedsOrder.setRequiredQuantity(consumerSeedsOrderDto.getRequiredQuantity());
        consumerSeedsOrder.setAddedDate(consumerSeedsOrderDto.getAddedDate());
        consumerSeedsOrder.setProductCategory(ConsumerSeedsOrderDto.ProductCategory.valueOf(consumerSeedsOrderDto.getProductCategory()));
        consumerSeedsOrder.setActive(true);
        consumerSeedsOrder.setUser(user);
        consumerSeedsOrder.setSFProduct(sfProduct);
        consumerSeedsOrder.setConfirmed(false);
        consumerSeedsOrder.setRejected(false);

        ConsumerSeedsOrderAddResponse response = new ConsumerSeedsOrderAddResponse();
        try {
            // Save or update the offer
            ConsumerSeedsOrder savedOrder = consumerSeedsOrderRepository.save(consumerSeedsOrder);

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

    public ConsumerSeedsOrderGetResponse getConsumerSeedsOrderByUserId(int userID) {
        ConsumerSeedsOrderGetResponse response = new ConsumerSeedsOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerSeedsOrder> consumerSeedsOrderList = consumerSeedsOrderRepository.findByUser_UserID(userID);

            if (consumerSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerSeedsOrderDto> consumerSeedsOrderDtoList = consumerSeedsOrderList.stream()
                    .map(consumerSeedsOrder -> {
                        ConsumerSeedsOrderDto dto = new ConsumerSeedsOrderDto();
                        dto.setOrderID(consumerSeedsOrder.getOrderID());
                        dto.setProductName(consumerSeedsOrder.getProductName());
                        dto.setPrice(consumerSeedsOrder.getPrice());
                        dto.setRequiredQuantity(consumerSeedsOrder.getRequiredQuantity());
                        dto.setRejected(consumerSeedsOrder.isRejected());
                        dto.setAddedDate(consumerSeedsOrder.getAddedDate());
                        dto.setDescription(consumerSeedsOrder.getDescription());
                        dto.setActive(consumerSeedsOrder.isActive());
                        dto.setConfirmed(consumerSeedsOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(consumerSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(consumerSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(consumerSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(consumerSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(consumerSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(consumerSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(consumerSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(consumerSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(consumerSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(consumerSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(consumerSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(consumerSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(consumerSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(consumerSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(consumerSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(consumerSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(consumerSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(consumerSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerSeedsOrderGetResponse(consumerSeedsOrderDtoList);
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


    public ConsumerSeedsOrderGetResponse getConsumerSeedsOrderBySeedsSellerUserId(int seedsSellerUserID) {
        ConsumerSeedsOrderGetResponse response = new ConsumerSeedsOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<ConsumerSeedsOrder> consumerSeedsOrderList = consumerSeedsOrderRepository.findBySFProduct_User_UserID(seedsSellerUserID);

            if (consumerSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + seedsSellerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<ConsumerSeedsOrderDto> consumerSeedsOrderDtoList = consumerSeedsOrderList.stream()
                    .map(consumerSeedsOrder -> {
                        ConsumerSeedsOrderDto dto = new ConsumerSeedsOrderDto();
                        dto.setOrderID(consumerSeedsOrder.getOrderID());
                        dto.setProductName(consumerSeedsOrder.getProductName());
                        dto.setPrice(consumerSeedsOrder.getPrice());
                        dto.setRequiredQuantity(consumerSeedsOrder.getRequiredQuantity());
                        dto.setRejected(consumerSeedsOrder.isRejected());
                        dto.setAddedDate(consumerSeedsOrder.getAddedDate());
                        dto.setDescription(consumerSeedsOrder.getDescription());
                        dto.setActive(consumerSeedsOrder.isActive());
                        dto.setConfirmed(consumerSeedsOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(consumerSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(consumerSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(consumerSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(consumerSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(consumerSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(consumerSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(consumerSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(consumerSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(consumerSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(consumerSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(consumerSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(consumerSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(consumerSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(consumerSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(consumerSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(consumerSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(consumerSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(consumerSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerSeedsOrderGetResponse(consumerSeedsOrderDtoList);
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

    public ConsumerSeedsOrderGetResponse getConsumerSeedsOrdersByProductId(int productID) {
        ConsumerSeedsOrderGetResponse response = new ConsumerSeedsOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerSeedsOrder> consumerSeedsOrderList = consumerSeedsOrderRepository.findBySFProduct_ProductID(productID);

            if (consumerSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerSeedsOrderDto> consumerSeedsOrderDtoList = consumerSeedsOrderList.stream()
                    .map(consumerSeedsOrder -> {
                        ConsumerSeedsOrderDto dto = new ConsumerSeedsOrderDto();
                        dto.setOrderID(consumerSeedsOrder.getOrderID());
                        dto.setProductName(consumerSeedsOrder.getProductName());
                        dto.setPrice(consumerSeedsOrder.getPrice());
                        dto.setRequiredQuantity(consumerSeedsOrder.getRequiredQuantity());
                        dto.setRejected(consumerSeedsOrder.isRejected());
                        dto.setAddedDate(consumerSeedsOrder.getAddedDate());
                        dto.setDescription(consumerSeedsOrder.getDescription());
                        dto.setActive(consumerSeedsOrder.isActive());
                        dto.setConfirmed(consumerSeedsOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(consumerSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(consumerSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(consumerSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(consumerSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(consumerSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(consumerSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(consumerSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(consumerSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(consumerSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(consumerSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(consumerSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(consumerSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(consumerSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(consumerSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(consumerSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(consumerSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(consumerSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(consumerSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerSeedsOrderGetResponse(consumerSeedsOrderDtoList);
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
    public ConsumerSeedsOrderDeleteResponse DeleteConsumerSeedsOrderResponse(int orderID) {
        ConsumerSeedsOrderDeleteResponse response = new ConsumerSeedsOrderDeleteResponse();

        //calculation part
        ConsumerSeedsOrder consumerSeedsOrder;
        consumerSeedsOrder = consumerSeedsOrderRepository.findByOrderID(orderID);



        try {
            consumerSeedsOrder.setActive(false);
            consumerSeedsOrderRepository.save(consumerSeedsOrder);
            response.setConsumerSeedsOrderDeleteResponse(consumerSeedsOrder);
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
    public ConsumerSeedsOrderConfirmResponse ConfirmConsumerSeedsOrderResponse(int orderID) {
        ConsumerSeedsOrderConfirmResponse response = new ConsumerSeedsOrderConfirmResponse();

        //calculation part
        ConsumerSeedsOrder consumerSeedsOrder;
        consumerSeedsOrder = consumerSeedsOrderRepository.findByOrderID(orderID);



        try {
            consumerSeedsOrder.setConfirmed(true);
            consumerSeedsOrderRepository.save(consumerSeedsOrder);
            response.setConsumerSeedsOrderConfirmResponse(consumerSeedsOrder);
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
    public ConsumerSeedsOrderRejectResponse RejectConsumerSeedsOrderResponse(int orderID) {
        ConsumerSeedsOrderRejectResponse response = new ConsumerSeedsOrderRejectResponse();

        //calculation part
        ConsumerSeedsOrder consumerSeedsOrder;
        consumerSeedsOrder = consumerSeedsOrderRepository.findByOrderID(orderID);



        try {
            consumerSeedsOrder.setRejected(true);
            consumerSeedsOrderRepository.save(consumerSeedsOrder);
            response.setConsumerSeedsOrderRejectResponse(consumerSeedsOrder);
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

