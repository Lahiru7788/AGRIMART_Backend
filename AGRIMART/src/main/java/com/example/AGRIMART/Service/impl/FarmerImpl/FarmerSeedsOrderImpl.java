package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerSeedsOrderRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerSeedsOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerSeedsOrderImpl implements FarmerSeedsOrderService {

    @Autowired
    private FarmerSeedsOrderRepository farmerSeedsOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SFProductRepository sfProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerSeedsOrderAddResponse saveOrUpdate(FarmerSeedsOrderDto farmerSeedsOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FarmerSeedsOrderAddResponse response = new FarmerSeedsOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SFProduct> productOptionalSf = sfProductRepository.findByProductID(farmerSeedsOrderDto.getProductID());

        if (userOptional.isEmpty()) {
            FarmerSeedsOrderAddResponse response = new FarmerSeedsOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }


        if (productOptionalSf.isEmpty()) {
            FarmerSeedsOrderAddResponse response = new FarmerSeedsOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        SFProduct sfProduct = productOptionalSf.get();

        // Check if there's an existing offer for this product
        Optional<FarmerSeedsOrder> existingOrders = farmerSeedsOrderRepository.findById(farmerSeedsOrderDto.getOrderID());

        FarmerSeedsOrder farmerSeedsOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            farmerSeedsOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            farmerSeedsOrder = new FarmerSeedsOrder();
            farmerSeedsOrder.setUser(user);
            farmerSeedsOrder.setSFProduct(sfProduct);
            actionPerformed = "added";
        }

        // Update fields
        farmerSeedsOrder.setProductName(farmerSeedsOrderDto.getProductName());
        farmerSeedsOrder.setPrice(farmerSeedsOrderDto.getPrice());
        farmerSeedsOrder.setDescription(farmerSeedsOrderDto.getDescription());
        farmerSeedsOrder.setRequiredQuantity(farmerSeedsOrderDto.getRequiredQuantity());
        farmerSeedsOrder.setAddedDate(farmerSeedsOrderDto.getAddedDate());
        farmerSeedsOrder.setProductCategory(FarmerSeedsOrderDto.ProductCategory.valueOf(farmerSeedsOrderDto.getProductCategory()));
        farmerSeedsOrder.setActive(true);
        farmerSeedsOrder.setUser(user);
        farmerSeedsOrder.setSFProduct(sfProduct);
        farmerSeedsOrder.setConfirmed(false);
        farmerSeedsOrder.setRejected(false);
        farmerSeedsOrder.setAddedToCart(false);
        farmerSeedsOrder.setRemovedFromCart(false);
        farmerSeedsOrder.setPaid(false);

        FarmerSeedsOrderAddResponse response = new FarmerSeedsOrderAddResponse();
        try {
            // Save or update the offer
            FarmerSeedsOrder savedOrder = farmerSeedsOrderRepository.save(farmerSeedsOrder);

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

    public FarmerSeedsOrderGetResponse getFarmerSeedsOrderByUserId(int userID) {
        FarmerSeedsOrderGetResponse response = new FarmerSeedsOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerSeedsOrder> farmerSeedsOrderList = farmerSeedsOrderRepository.findByUser_UserID(userID);

            if (farmerSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerSeedsOrderDto> farmerSeedsOrderDtoList = farmerSeedsOrderList.stream()
                    .map(farmerSeedsOrder -> {
                        FarmerSeedsOrderDto dto = new FarmerSeedsOrderDto();
                        dto.setOrderID(farmerSeedsOrder.getOrderID());
                        dto.setProductName(farmerSeedsOrder.getProductName());
                        dto.setPrice(farmerSeedsOrder.getPrice());
                        dto.setRequiredQuantity(farmerSeedsOrder.getRequiredQuantity());
                        dto.setRejected(farmerSeedsOrder.isRejected());
                        dto.setAddedDate(farmerSeedsOrder.getAddedDate());
                        dto.setDescription(farmerSeedsOrder.getDescription());
                        dto.setActive(farmerSeedsOrder.isActive());
                        dto.setConfirmed(farmerSeedsOrder.isConfirmed());
                        dto.setAddedToCart(farmerSeedsOrder.isAddedToCart());
                        dto.setRemovedFromCart(farmerSeedsOrder.isRemovedFromCart());
                        dto.setPaid(farmerSeedsOrder.isPaid());
                        dto.setProductCategory(String.valueOf(farmerSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(farmerSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(farmerSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(farmerSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(farmerSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(farmerSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(farmerSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(farmerSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(farmerSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(farmerSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(farmerSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(farmerSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(farmerSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(farmerSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(farmerSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(farmerSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(farmerSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(farmerSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerSeedsOrderGetResponse(farmerSeedsOrderDtoList);
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


    public FarmerSeedsOrderGetResponse getFarmerSeedsOrderBySeedsSellerUserId(int seedsSellerUserID) {
        FarmerSeedsOrderGetResponse response = new FarmerSeedsOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<FarmerSeedsOrder> farmerSeedsOrderList = farmerSeedsOrderRepository.findBySFProduct_User_UserID(seedsSellerUserID);

            if (farmerSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + seedsSellerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<FarmerSeedsOrderDto> farmerSeedsOrderDtoList = farmerSeedsOrderList.stream()
                    .map(farmerSeedsOrder -> {
                        FarmerSeedsOrderDto dto = new FarmerSeedsOrderDto();
                        dto.setOrderID(farmerSeedsOrder.getOrderID());
                        dto.setProductName(farmerSeedsOrder.getProductName());
                        dto.setPrice(farmerSeedsOrder.getPrice());
                        dto.setRequiredQuantity(farmerSeedsOrder.getRequiredQuantity());
                        dto.setRejected(farmerSeedsOrder.isRejected());
                        dto.setAddedDate(farmerSeedsOrder.getAddedDate());
                        dto.setDescription(farmerSeedsOrder.getDescription());
                        dto.setActive(farmerSeedsOrder.isActive());
                        dto.setConfirmed(farmerSeedsOrder.isConfirmed());
                        dto.setAddedToCart(farmerSeedsOrder.isAddedToCart());
                        dto.setRemovedFromCart(farmerSeedsOrder.isRemovedFromCart());
                        dto.setPaid(farmerSeedsOrder.isPaid());
                        dto.setProductCategory(String.valueOf(farmerSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(farmerSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(farmerSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(farmerSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(farmerSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(farmerSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(farmerSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(farmerSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(farmerSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(farmerSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(farmerSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(farmerSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(farmerSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(farmerSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(farmerSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(farmerSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(farmerSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(farmerSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerSeedsOrderGetResponse(farmerSeedsOrderDtoList);
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

    public FarmerSeedsOrderGetResponse getFarmerSeedsOrdersByProductId(int productID) {
        FarmerSeedsOrderGetResponse response = new FarmerSeedsOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerSeedsOrder> farmerSeedsOrderList = farmerSeedsOrderRepository.findBySFProduct_ProductID(productID);

            if (farmerSeedsOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerSeedsOrderDto> farmerSeedsOrderDtoList = farmerSeedsOrderList.stream()
                    .map(farmerSeedsOrder -> {
                        FarmerSeedsOrderDto dto = new FarmerSeedsOrderDto();
                        dto.setOrderID(farmerSeedsOrder.getOrderID());
                        dto.setProductName(farmerSeedsOrder.getProductName());
                        dto.setPrice(farmerSeedsOrder.getPrice());
                        dto.setRequiredQuantity(farmerSeedsOrder.getRequiredQuantity());
                        dto.setRejected(farmerSeedsOrder.isRejected());
                        dto.setAddedDate(farmerSeedsOrder.getAddedDate());
                        dto.setDescription(farmerSeedsOrder.getDescription());
                        dto.setActive(farmerSeedsOrder.isActive());
                        dto.setConfirmed(farmerSeedsOrder.isConfirmed());
                        dto.setAddedToCart(farmerSeedsOrder.isAddedToCart());
                        dto.setRemovedFromCart(farmerSeedsOrder.isRemovedFromCart());
                        dto.setPaid(farmerSeedsOrder.isPaid());
                        dto.setProductCategory(String.valueOf(farmerSeedsOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerSeedsOrder.getUser().getUserID());
                        userDto.setUserEmail(farmerSeedsOrder.getUser().getUserEmail());
                        userDto.setFirstName(farmerSeedsOrder.getUser().getFirstName());
                        userDto.setLastName(farmerSeedsOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerSeedsOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        SFProductDto sfProductDto = new SFProductDto();
                        sfProductDto.setProductID(farmerSeedsOrder.getSFProduct().getProductID());
                        sfProductDto.setProductName(farmerSeedsOrder.getSFProduct().getProductName());
                        sfProductDto.setPrice(farmerSeedsOrder.getSFProduct().getPrice());
                        sfProductDto.setAvailableQuantity(farmerSeedsOrder.getSFProduct().getAvailableQuantity());
                        sfProductDto.setMinimumQuantity(farmerSeedsOrder.getSFProduct().getMinimumQuantity());
                        sfProductDto.setAddedDate(farmerSeedsOrder.getSFProduct().getAddedDate());
                        sfProductDto.setDescription(farmerSeedsOrder.getSFProduct().getDescription());
                        sfProductDto.setActive(farmerSeedsOrder.getSFProduct().isActive());
                        sfProductDto.setDeleted(farmerSeedsOrder.getSFProduct().isDeleted());
                        sfProductDto.setQuantityLowered(farmerSeedsOrder.getSFProduct().isQuantityLowered());
                        sfProductDto.setProductCategory(String.valueOf(farmerSeedsOrder.getSFProduct().getProductCategory()));

                        UserDto seedsSellerUserDto = new UserDto();
                        seedsSellerUserDto.setUserID(farmerSeedsOrder.getSFProduct().getUser().getUserID());
                        seedsSellerUserDto.setUserEmail(farmerSeedsOrder.getSFProduct().getUser().getUserEmail());
                        seedsSellerUserDto.setFirstName(farmerSeedsOrder.getSFProduct().getUser().getFirstName());
                        seedsSellerUserDto.setLastName(farmerSeedsOrder.getSFProduct().getUser().getLastName());
                        seedsSellerUserDto.setUserType(String.valueOf(farmerSeedsOrder.getSFProduct().getUser().getUserType()));
                        sfProductDto.setUser(seedsSellerUserDto);

                        dto.setSfProduct(sfProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerSeedsOrderGetResponse(farmerSeedsOrderDtoList);
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
    public FarmerSeedsOrderDeleteResponse DeleteFarmerSeedsOrderResponse(int orderID) {
        FarmerSeedsOrderDeleteResponse response = new FarmerSeedsOrderDeleteResponse();

        //calculation part
        FarmerSeedsOrder farmerSeedsOrder;
        farmerSeedsOrder = farmerSeedsOrderRepository.findByOrderID(orderID);



        try {
            farmerSeedsOrder.setActive(false);
            farmerSeedsOrderRepository.save(farmerSeedsOrder);
            response.setFarmerSeedsOrderDeleteResponse(farmerSeedsOrder);
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
    public FarmerSeedsOrderConfirmResponse ConfirmFarmerSeedsOrderResponse(int orderID) {
        FarmerSeedsOrderConfirmResponse response = new FarmerSeedsOrderConfirmResponse();

        //calculation part
        FarmerSeedsOrder farmerSeedsOrder;
        farmerSeedsOrder = farmerSeedsOrderRepository.findByOrderID(orderID);



        try {
            farmerSeedsOrder.setConfirmed(true);
            farmerSeedsOrderRepository.save(farmerSeedsOrder);
            response.setFarmerSeedsOrderConfirmResponse(farmerSeedsOrder);
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
    public FarmerSeedsOrderRejectResponse RejectFarmerSeedsOrderResponse(int orderID) {
        FarmerSeedsOrderRejectResponse response = new FarmerSeedsOrderRejectResponse();

        //calculation part
        FarmerSeedsOrder farmerSeedsOrder;
        farmerSeedsOrder = farmerSeedsOrderRepository.findByOrderID(orderID);



        try {
            farmerSeedsOrder.setRejected(true);
            farmerSeedsOrderRepository.save(farmerSeedsOrder);
            response.setFarmerSeedsOrderRejectResponse(farmerSeedsOrder);
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
    public FarmerSeedsOrderAddToCartResponse AddToCartFarmerSeedsOrderResponse(int orderID) {
        FarmerSeedsOrderAddToCartResponse response = new FarmerSeedsOrderAddToCartResponse();

        //calculation part
        FarmerSeedsOrder farmerSeedsOrder;
        farmerSeedsOrder = farmerSeedsOrderRepository.findByOrderID(orderID);



        try {
            SFProduct sfProduct = farmerSeedsOrder.getSFProduct();
            double requiredQuantity = farmerSeedsOrder.getRequiredQuantity();
            double availableQuantity = sfProduct.getAvailableQuantity();
            if (availableQuantity >= requiredQuantity) {
                // Update the available quantity
                double newAvailableQuantity = availableQuantity - requiredQuantity;
                sfProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

                // Save the updated product
                sfProductRepository.save(sfProduct); // Assuming you have a productRepository

                // Mark order as added to cart
                farmerSeedsOrder.setAddedToCart(true);
                farmerSeedsOrderRepository.save(farmerSeedsOrder);
                response.setFarmerSeedsOrderAddToCartResponse(farmerSeedsOrder);
                response.setMessage("product Id : " + orderID + " item delete successfully");
                response.setStatus("200");
                response.setResponseCode("11000");
            } else {
                // Not enough quantity available
                response.setMessage("Insufficient quantity available. Required: " + requiredQuantity + ", Available: " + availableQuantity);
                response.setStatus("400");
                response.setResponseCode("11002");
            }
        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerSeedsOrderRemovedFromCartResponse RemovedFromCartFarmerSeedsOrderResponse(int orderID) {
        FarmerSeedsOrderRemovedFromCartResponse response = new FarmerSeedsOrderRemovedFromCartResponse();

        //calculation part
        FarmerSeedsOrder farmerSeedsOrder;
        farmerSeedsOrder = farmerSeedsOrderRepository.findByOrderID(orderID);



        try {
            SFProduct sfProduct = farmerSeedsOrder.getSFProduct();
            double requiredQuantity = farmerSeedsOrder.getRequiredQuantity();
            double availableQuantity = sfProduct.getAvailableQuantity();
            // Update the available quantity
            double newAvailableQuantity = availableQuantity + requiredQuantity;
            sfProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

            // Save the updated product
            sfProductRepository.save(sfProduct); // Assuming you have a productRepository

            // Mark order as added to cart
            farmerSeedsOrder.setRemovedFromCart(true);
            farmerSeedsOrderRepository.save(farmerSeedsOrder);
            response.setFarmerSeedsOrderRemovedFromCartResponse(farmerSeedsOrder);
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
    public FarmerSeedsOrderPaymentResponse PaymentFarmerSeedsOrderResponse(int orderID) {
        FarmerSeedsOrderPaymentResponse response = new FarmerSeedsOrderPaymentResponse();

        //calculation part
        FarmerSeedsOrder farmerSeedsOrder;
        farmerSeedsOrder = farmerSeedsOrderRepository.findByOrderID(orderID);



        try {
            farmerSeedsOrder.setPaid(true);
            farmerSeedsOrderRepository.save(farmerSeedsOrder);
            response.setFarmerSeedsOrderPaymentResponse(farmerSeedsOrder);
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
