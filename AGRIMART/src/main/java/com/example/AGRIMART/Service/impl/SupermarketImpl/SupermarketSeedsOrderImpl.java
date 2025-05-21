package com.example.AGRIMART.Service.impl.SupermarketImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketSeedsOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SupermarketService.SupermarketSeedsOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupermarketSeedsOrderImpl implements SupermarketSeedsOrderService {

    @Autowired
    private SupermarketSeedsOrderRepository supermarketSeedsOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SFProductRepository sfProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SupermarketSeedsOrderAddResponse saveOrUpdate(SupermarketSeedsOrderDto supermarketSeedsOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            SupermarketSeedsOrderAddResponse response = new SupermarketSeedsOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SFProduct> productOptionalSf = sfProductRepository.findByProductName(productID);

        if (userOptional.isEmpty()) {
            SupermarketSeedsOrderAddResponse response = new SupermarketSeedsOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }


        if (productOptionalSf.isEmpty()) {
            SupermarketSeedsOrderAddResponse response = new SupermarketSeedsOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        SFProduct sfProduct = productOptionalSf.get();

        // Check if there's an existing offer for this product
        Optional<SupermarketSeedsOrder> existingOrders = supermarketSeedsOrderRepository.findById(supermarketSeedsOrderDto.getOrderID());

       SupermarketSeedsOrder supermarketSeedsOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            supermarketSeedsOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            supermarketSeedsOrder = new SupermarketSeedsOrder();
            supermarketSeedsOrder.setUser(user);
            supermarketSeedsOrder.setSFProduct(sfProduct);
            actionPerformed = "added";
        }

        // Update fields
        supermarketSeedsOrder.setProductName(supermarketSeedsOrderDto.getProductName());
        supermarketSeedsOrder.setPrice(supermarketSeedsOrderDto.getPrice());
        supermarketSeedsOrder.setDescription(supermarketSeedsOrderDto.getDescription());
        supermarketSeedsOrder.setRequiredQuantity(supermarketSeedsOrderDto.getRequiredQuantity());
        supermarketSeedsOrder.setAddedDate(supermarketSeedsOrderDto.getAddedDate());
        supermarketSeedsOrder.setProductCategory(ConsumerSeedsOrderDto.ProductCategory.valueOf(supermarketSeedsOrderDto.getProductCategory()));
        supermarketSeedsOrder.setActive(true);
        supermarketSeedsOrder.setUser(user);
        supermarketSeedsOrder.setSFProduct(sfProduct);
        supermarketSeedsOrder.setConfirmed(false);
        supermarketSeedsOrder.setRejected(false);

        SupermarketSeedsOrderAddResponse response = new SupermarketSeedsOrderAddResponse();
        try {
            // Save or update the offer
            SupermarketSeedsOrder savedOrder = supermarketSeedsOrderRepository.save(supermarketSeedsOrder);

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

    public SupermarketSeedsOrderGetResponse getSupermarketSeedsOrderByUserId(int userID) {
        SupermarketSeedsOrderGetResponse response = new SupermarketSeedsOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SupermarketSeedsOrder> supermarketSeedsOrderList = supermarketSeedsOrderRepository.findByUser_UserID(userID);

            if (supermarketSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SupermarketSeedsOrderDto> supermarketSeedsOrderDtoList = supermarketSeedsOrderList.stream()
                    .map(supermarketSeedsOrder -> {
                        SupermarketSeedsOrderDto dto = new SupermarketSeedsOrderDto();
                        dto.setOrderID(supermarketSeedsOrder.getOrderID());
                        dto.setProductName(supermarketSeedsOrder.getProductName());
                        dto.setPrice(supermarketSeedsOrder.getPrice());
                        dto.setRequiredQuantity(supermarketSeedsOrder.getRequiredQuantity());
                        dto.setRejected(supermarketSeedsOrder.isRejected());
                        dto.setAddedDate(supermarketSeedsOrder.getAddedDate());
                        dto.setDescription(supermarketSeedsOrder.getDescription());
                        dto.setActive(supermarketSeedsOrder.isActive());
                        dto.setConfirmed(supermarketSeedsOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(supermarketSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(supermarketSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(supermarketSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(supermarketSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(supermarketSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(supermarketSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(supermarketSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(supermarketSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(supermarketSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(supermarketSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(supermarketSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(supermarketSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(supermarketSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(supermarketSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(supermarketSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(supermarketSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketSeedsOrderGetResponse(supermarketSeedsOrderDtoList);
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


    public SupermarketSeedsOrderGetResponse getSupermarketSeedsOrderBySeedsSellerUserId(int seedsSellerUserID) {
        SupermarketSeedsOrderGetResponse response = new SupermarketSeedsOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<SupermarketSeedsOrder> supermarketSeedsOrderList = supermarketSeedsOrderRepository.findBySFProduct_User_UserID(seedsSellerUserID);

            if (supermarketSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + seedsSellerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<SupermarketSeedsOrderDto> supermarketSeedsOrderDtoList = supermarketSeedsOrderList.stream()
                    .map(supermarketSeedsOrder -> {
                        SupermarketSeedsOrderDto dto = new SupermarketSeedsOrderDto();
                        dto.setOrderID(supermarketSeedsOrder.getOrderID());
                        dto.setProductName(supermarketSeedsOrder.getProductName());
                        dto.setPrice(supermarketSeedsOrder.getPrice());
                        dto.setRequiredQuantity(supermarketSeedsOrder.getRequiredQuantity());
                        dto.setRejected(supermarketSeedsOrder.isRejected());
                        dto.setAddedDate(supermarketSeedsOrder.getAddedDate());
                        dto.setDescription(supermarketSeedsOrder.getDescription());
                        dto.setActive(supermarketSeedsOrder.isActive());
                        dto.setConfirmed(supermarketSeedsOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(supermarketSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(supermarketSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(supermarketSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(supermarketSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(supermarketSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(supermarketSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(supermarketSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(supermarketSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(supermarketSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(supermarketSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(supermarketSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(supermarketSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(supermarketSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(supermarketSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(supermarketSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(supermarketSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketSeedsOrderGetResponse(supermarketSeedsOrderDtoList);
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

    public SupermarketSeedsOrderGetResponse getSupermarketSeedsOrdersByProductId(int productID) {
        SupermarketSeedsOrderGetResponse response = new SupermarketSeedsOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SupermarketSeedsOrder> supermarketSeedsOrderList = supermarketSeedsOrderRepository.findBySFProduct_ProductID(productID);

            if (supermarketSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SupermarketSeedsOrderDto> supermarketSeedsOrderDtoList = supermarketSeedsOrderList.stream()
                    .map(supermarketSeedsOrder -> {
                        SupermarketSeedsOrderDto dto = new SupermarketSeedsOrderDto();
                        dto.setOrderID(supermarketSeedsOrder.getOrderID());
                        dto.setProductName(supermarketSeedsOrder.getProductName());
                        dto.setPrice(supermarketSeedsOrder.getPrice());
                        dto.setRequiredQuantity(supermarketSeedsOrder.getRequiredQuantity());
                        dto.setRejected(supermarketSeedsOrder.isRejected());
                        dto.setAddedDate(supermarketSeedsOrder.getAddedDate());
                        dto.setDescription(supermarketSeedsOrder.getDescription());
                        dto.setActive(supermarketSeedsOrder.isActive());
                        dto.setConfirmed(supermarketSeedsOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(supermarketSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(supermarketSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(supermarketSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(supermarketSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(supermarketSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(supermarketSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(supermarketSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(supermarketSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(supermarketSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(supermarketSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(supermarketSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(supermarketSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(supermarketSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(supermarketSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(supermarketSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(supermarketSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketSeedsOrderGetResponse(supermarketSeedsOrderDtoList);
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
    public SupermarketSeedsOrderDeleteResponse DeleteSupermarketSeedsOrderResponse(int orderID) {
        SupermarketSeedsOrderDeleteResponse response = new SupermarketSeedsOrderDeleteResponse();

        //calculation part
        SupermarketSeedsOrder supermarketSeedsOrder;
        supermarketSeedsOrder = supermarketSeedsOrderRepository.findByOrderID(orderID);



        try {
            supermarketSeedsOrder.setActive(false);
            supermarketSeedsOrderRepository.save(supermarketSeedsOrder);
            response.setSupermarketSeedsOrderDeleteResponse(supermarketSeedsOrder);
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
    public SupermarketSeedsOrderConfirmResponse ConfirmSupermarketSeedsOrderResponse(int orderID) {
        SupermarketSeedsOrderConfirmResponse response = new SupermarketSeedsOrderConfirmResponse();

        //calculation part
        SupermarketSeedsOrder supermarketSeedsOrder;
        supermarketSeedsOrder = supermarketSeedsOrderRepository.findByOrderID(orderID);


        try {
            supermarketSeedsOrder.setConfirmed(true);
            supermarketSeedsOrderRepository.save(supermarketSeedsOrder);
            response.setSupermarketSeedsOrderConfirmResponse(supermarketSeedsOrder);
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
    public SupermarketSeedsOrderRejectResponse RejectSupermarketSeedsOrderResponse(int orderID) {
        SupermarketSeedsOrderRejectResponse response = new SupermarketSeedsOrderRejectResponse();

        //calculation part
        SupermarketSeedsOrder supermarketSeedsOrder;
        supermarketSeedsOrder = supermarketSeedsOrderRepository.findByOrderID(orderID);



        try {
            supermarketSeedsOrder.setRejected(true);
            supermarketSeedsOrderRepository.save(supermarketSeedsOrder);
            response.setSupermarketSeedsOrderRejectResponse(supermarketSeedsOrder);
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
