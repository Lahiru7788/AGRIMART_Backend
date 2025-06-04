package com.example.AGRIMART.Service.impl.SFImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SFResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SFEntity.SFOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.SFRepository.SFOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import com.example.AGRIMART.Service.SFService.SFOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SFOrderImpl implements SFOrderService {

    @Autowired
    private SFOrderRepository sfOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SFOrderAddResponse saveOrUpdate(SFOrderDto sfOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            SFOrderAddResponse response = new SFOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductID(sfOrderDto.getProductID());

        if (userOptional.isEmpty()) {
            SFOrderAddResponse response = new SFOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            SFOrderAddResponse response = new SFOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<SFOrder> existingOrders = sfOrderRepository.findById(sfOrderDto.getOrderID());

        SFOrder sfOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            sfOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            sfOrder = new SFOrder();
            sfOrder.setUser(user);
            sfOrder.setFarmerProduct(farmerProduct);
            actionPerformed = "added";
        }

        // Update fields
        sfOrder.setProductName(sfOrderDto.getProductName());
        sfOrder.setPrice(sfOrderDto.getPrice());
        sfOrder.setDescription(sfOrderDto.getDescription());
        sfOrder.setRequiredQuantity(sfOrderDto.getRequiredQuantity());
        sfOrder.setAddedDate(sfOrderDto.getAddedDate());
        sfOrder.setProductCategory(SFOrderDto.ProductCategory.valueOf(sfOrderDto.getProductCategory()));
        sfOrder.setActive(true);
        sfOrder.setUser(user);
        sfOrder.setFarmerProduct(farmerProduct);
        sfOrder.setConfirmed(false);
        sfOrder.setRejected(false);
        sfOrder.setAddedToCart(false);
        sfOrder.setRemovedFromCart(false);
        sfOrder.setPaid(false);

        SFOrderAddResponse response = new SFOrderAddResponse();
        try {
            // Save or update the offer
            SFOrder savedOrder = sfOrderRepository.save(sfOrder);

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

    public SFOrderGetResponse getSFOrderByUserId(int userID) {
        SFOrderGetResponse response = new SFOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SFOrder> sfOrderList = sfOrderRepository.findByUser_UserID(userID);

            if (sfOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SFOrderDto> sfOrderDtoList = sfOrderList.stream()
                    .map(sfOrder -> {
                        SFOrderDto dto = new SFOrderDto();
                        dto.setOrderID(sfOrder.getOrderID());
                        dto.setProductName(sfOrder.getProductName());
                        dto.setPrice(sfOrder.getPrice());
                        dto.setRequiredQuantity(sfOrder.getRequiredQuantity());
                        dto.setRejected(sfOrder.isRejected());
                        dto.setAddedDate(sfOrder.getAddedDate());
                        dto.setDescription(sfOrder.getDescription());
                        dto.setActive(sfOrder.isActive());
                        dto.setConfirmed(sfOrder.isConfirmed());
                        dto.setAddedToCart(sfOrder.isAddedToCart());
                        dto.setRemovedFromCart(sfOrder.isRemovedFromCart());
                        dto.setPaid(sfOrder.isPaid());
                        dto.setProductCategory(String.valueOf(sfOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(sfOrder.getUser().getUserID());
                        userDto.setUserEmail(sfOrder.getUser().getUserEmail());
                        userDto.setFirstName(sfOrder.getUser().getFirstName());
                        userDto.setLastName(sfOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(sfOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(sfOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(sfOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(sfOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(sfOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(sfOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(sfOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(sfOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(sfOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(sfOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(sfOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(sfOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(sfOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(sfOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(sfOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(sfOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(sfOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSfOrderGetResponse(sfOrderDtoList);
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

    public SFOrderGetResponse getSFOrderByFarmerUserId(int farmerUserID) {
        SFOrderGetResponse response = new SFOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<SFOrder> sfOrderList = sfOrderRepository.findByFarmerProduct_User_UserID(farmerUserID);

            if (sfOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + farmerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<SFOrderDto> sfOrderDtoList = sfOrderList.stream()
                    .map(sfOrder -> {
                        SFOrderDto dto = new SFOrderDto();
                        dto.setOrderID(sfOrder.getOrderID());
                        dto.setProductName(sfOrder.getProductName());
                        dto.setPrice(sfOrder.getPrice());
                        dto.setRequiredQuantity(sfOrder.getRequiredQuantity());
                        dto.setRejected(sfOrder.isRejected());
                        dto.setAddedDate(sfOrder.getAddedDate());
                        dto.setDescription(sfOrder.getDescription());
                        dto.setActive(sfOrder.isActive());
                        dto.setConfirmed(sfOrder.isConfirmed());
                        dto.setAddedToCart(sfOrder.isAddedToCart());
                        dto.setRemovedFromCart(sfOrder.isRemovedFromCart());
                        dto.setPaid(sfOrder.isPaid());
                        dto.setProductCategory(String.valueOf(sfOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(sfOrder.getUser().getUserID());
                        userDto.setUserEmail(sfOrder.getUser().getUserEmail());
                        userDto.setFirstName(sfOrder.getUser().getFirstName());
                        userDto.setLastName(sfOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(sfOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(sfOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(sfOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(sfOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(sfOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(sfOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(sfOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(sfOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(sfOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(sfOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(sfOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(sfOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(sfOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(sfOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(sfOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(sfOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(sfOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSfOrderGetResponse(sfOrderDtoList);
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


    public SFOrderGetResponse getSFOrdersByProductId(int productID) {
        SFOrderGetResponse response = new SFOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SFOrder> sfOrderList = sfOrderRepository.findByFarmerProduct_ProductID(productID);

            if (sfOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SFOrderDto> sfOrderDtoList = sfOrderList.stream()
                    .map(sfOrder -> {
                        SFOrderDto dto = new SFOrderDto();
                        dto.setOrderID(sfOrder.getOrderID());
                        dto.setProductName(sfOrder.getProductName());
                        dto.setPrice(sfOrder.getPrice());
                        dto.setRequiredQuantity(sfOrder.getRequiredQuantity());
                        dto.setRejected(sfOrder.isRejected());
                        dto.setAddedDate(sfOrder.getAddedDate());
                        dto.setDescription(sfOrder.getDescription());
                        dto.setActive(sfOrder.isActive());
                        dto.setConfirmed(sfOrder.isConfirmed());
                        dto.setAddedToCart(sfOrder.isAddedToCart());
                        dto.setRemovedFromCart(sfOrder.isRemovedFromCart());
                        dto.setPaid(sfOrder.isPaid());
                        dto.setProductCategory(String.valueOf(sfOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(sfOrder.getUser().getUserID());
                        userDto.setUserEmail(sfOrder.getUser().getUserEmail());
                        userDto.setFirstName(sfOrder.getUser().getFirstName());
                        userDto.setLastName(sfOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(sfOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(sfOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(sfOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(sfOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(sfOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(sfOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(sfOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(sfOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(sfOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(sfOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(sfOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(sfOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(sfOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(sfOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(sfOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(sfOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(sfOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSfOrderGetResponse(sfOrderDtoList);
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
    public SFOrderDeleteResponse DeleteSFOrderResponse(int orderID) {
        SFOrderDeleteResponse response = new SFOrderDeleteResponse();

        //calculation part
        SFOrder sfOrder;
        sfOrder = sfOrderRepository.findByOrderID(orderID);



        try {
            sfOrder.setActive(false);
            sfOrderRepository.save(sfOrder);
            response.setSfOrderDeleteResponse(sfOrder);
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
    public SFOrderConfirmResponse ConfirmSFOrderResponse(int orderID) {
        SFOrderConfirmResponse response = new SFOrderConfirmResponse();

        //calculation part
        SFOrder sfOrder;
        sfOrder = sfOrderRepository.findByOrderID(orderID);



        try {
            sfOrder.setConfirmed(true);
            sfOrderRepository.save(sfOrder);
            response.setSfOrderConfirmResponse(sfOrder);
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
    public SFOrderRejectResponse RejectSFOrderResponse(int orderID) {
        SFOrderRejectResponse response = new SFOrderRejectResponse();

        //calculation part
        SFOrder sfOrder;
        sfOrder = sfOrderRepository.findByOrderID(orderID);


        try {
            sfOrder.setRejected(true);
            sfOrderRepository.save(sfOrder);
            response.setSfOrderRejectResponse(sfOrder);
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
    public SFOrderAddToCartResponse AddToCartSFOrderResponse(int orderID) {
        SFOrderAddToCartResponse response = new SFOrderAddToCartResponse();

        //calculation part
        SFOrder sfOrder;
        sfOrder = sfOrderRepository.findByOrderID(orderID);



        try {
            FarmerProduct farmerProduct = sfOrder.getFarmerProduct();
            double requiredQuantity = sfOrder.getRequiredQuantity();
            double availableQuantity = farmerProduct.getAvailableQuantity();
            if (availableQuantity >= requiredQuantity) {
                // Update the available quantity
                double newAvailableQuantity = availableQuantity - requiredQuantity;
                farmerProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

                // Save the updated product
                farmerProductRepository.save(farmerProduct); // Assuming you have a productRepository

                // Mark order as added to cart
                sfOrder.setAddedToCart(true);
                sfOrderRepository.save(sfOrder);
                response.setSfOrderAddToCartResponse(sfOrder);
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
    public SFOrderRemovedFromCartResponse RemovedFromCartSFOrderResponse(int orderID) {
        SFOrderRemovedFromCartResponse response = new SFOrderRemovedFromCartResponse();

        //calculation part
        SFOrder sfOrder;
        sfOrder = sfOrderRepository.findByOrderID(orderID);



        try {
            FarmerProduct farmerProduct = sfOrder.getFarmerProduct();
            double requiredQuantity = sfOrder.getRequiredQuantity();
            double availableQuantity = farmerProduct.getAvailableQuantity();
            // Update the available quantity
            double newAvailableQuantity = availableQuantity + requiredQuantity;
            farmerProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

            // Save the updated product
            farmerProductRepository.save(farmerProduct); // Assuming you have a productRepository

            // Mark order as added to cart
            sfOrder.setRemovedFromCart(true);
            sfOrderRepository.save(sfOrder);
            response.setSfOrderRemovedFromCartResponse(sfOrder);
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
    public SFOrderPaymentResponse PaymentSFOrderResponse(int orderID) {
        SFOrderPaymentResponse response = new SFOrderPaymentResponse();

        //calculation part
        SFOrder sfOrder;
        sfOrder = sfOrderRepository.findByOrderID(orderID);



        try {
            sfOrder.setPaid(true);
            sfOrderRepository.save(sfOrder);
            response.setSfOrderPaymentResponse(sfOrder);
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
