package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerOrderImpl implements ConsumerOrderService {

    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerOrderAddResponse saveOrUpdate(ConsumerOrderDto consumerOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            ConsumerOrderAddResponse response = new ConsumerOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductName(productID);

        if (userOptional.isEmpty()) {
            ConsumerOrderAddResponse response = new ConsumerOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            ConsumerOrderAddResponse response = new ConsumerOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<ConsumerOrder> existingOrders = consumerOrderRepository.findById(consumerOrderDto.getOrderID());

        ConsumerOrder consumerOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            consumerOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            consumerOrder = new ConsumerOrder();
            consumerOrder.setUser(user);
            consumerOrder.setFarmerProduct(farmerProduct);
            actionPerformed = "added";
        }

        // Update fields
        consumerOrder.setProductName(consumerOrderDto.getProductName());
        consumerOrder.setPrice(consumerOrderDto.getPrice());
        consumerOrder.setDescription(consumerOrderDto.getDescription());
        consumerOrder.setRequiredQuantity(consumerOrderDto.getRequiredQuantity());
        consumerOrder.setAddedDate(consumerOrderDto.getAddedDate());
        consumerOrder.setProductCategory(ConsumerOrderDto.ProductCategory.valueOf(consumerOrderDto.getProductCategory()));
        consumerOrder.setActive(true);
        consumerOrder.setUser(user);
        consumerOrder.setFarmerProduct(farmerProduct);
        consumerOrder.setConfirmed(false);
        consumerOrder.setRejected(false);
        consumerOrder.setAddedToCart(false);
        consumerOrder.setRemovedFromCart(false);

        ConsumerOrderAddResponse response = new ConsumerOrderAddResponse();
        try {
            // Save or update the offer
            ConsumerOrder savedOrder = consumerOrderRepository.save(consumerOrder);

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

    public ConsumerOrderGetResponse getConsumerOrderByUserId(int userID) {
        ConsumerOrderGetResponse response = new ConsumerOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerOrder> consumerOrderList = consumerOrderRepository.findByUser_UserID(userID);

            if (consumerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerOrderDto> consumerOrderDtoList = consumerOrderList.stream()
                    .map(consumerOrder -> {
                        ConsumerOrderDto dto = new ConsumerOrderDto();
                        dto.setOrderID(consumerOrder.getOrderID());
                        dto.setProductName(consumerOrder.getProductName());
                        dto.setPrice(consumerOrder.getPrice());
                        dto.setRequiredQuantity(consumerOrder.getRequiredQuantity());
                        dto.setRejected(consumerOrder.isRejected());
                        dto.setAddedDate(consumerOrder.getAddedDate());
                        dto.setDescription(consumerOrder.getDescription());
                        dto.setActive(consumerOrder.isActive());
                        dto.setConfirmed(consumerOrder.isConfirmed());
                        dto.setAddedToCart(consumerOrder.isAddedToCart());
                        dto.setRemovedFromCart(consumerOrder.isRemovedFromCart());
                        dto.setProductCategory(String.valueOf(consumerOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerOrder.getUser().getFirstName());
                        userDto.setLastName(consumerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(consumerOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(consumerOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(consumerOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(consumerOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(consumerOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(consumerOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(consumerOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(consumerOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(consumerOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(consumerOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(consumerOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(consumerOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(consumerOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(consumerOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(consumerOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(consumerOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerOrderGetResponse(consumerOrderDtoList);
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

    public ConsumerOrderGetResponse getConsumerOrderByFarmerUserId(int farmerUserID) {
        ConsumerOrderGetResponse response = new ConsumerOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<ConsumerOrder> consumerOrderList = consumerOrderRepository.findByFarmerProduct_User_UserID(farmerUserID);

            if (consumerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + farmerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<ConsumerOrderDto> consumerOrderDtoList = consumerOrderList.stream()
                    .map(consumerOrder -> {
                        ConsumerOrderDto dto = new ConsumerOrderDto();
                        dto.setOrderID(consumerOrder.getOrderID());
                        dto.setProductName(consumerOrder.getProductName());
                        dto.setPrice(consumerOrder.getPrice());
                        dto.setRequiredQuantity(consumerOrder.getRequiredQuantity());
                        dto.setRejected(consumerOrder.isRejected());
                        dto.setAddedDate(consumerOrder.getAddedDate());
                        dto.setDescription(consumerOrder.getDescription());
                        dto.setActive(consumerOrder.isActive());
                        dto.setConfirmed(consumerOrder.isConfirmed());
                        dto.setAddedToCart(consumerOrder.isAddedToCart());
                        dto.setRemovedFromCart(consumerOrder.isRemovedFromCart());
                        dto.setProductCategory(String.valueOf(consumerOrder.getProductCategory()));

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerOrder.getUser().getFirstName());
                        userDto.setLastName(consumerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(consumerOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(consumerOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(consumerOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(consumerOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(consumerOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(consumerOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(consumerOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(consumerOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(consumerOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(consumerOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(consumerOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(consumerOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(consumerOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(consumerOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(consumerOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(consumerOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerOrderGetResponse(consumerOrderDtoList);
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


    public ConsumerOrderGetResponse getConsumerOrdersByProductId(int productID) {
        ConsumerOrderGetResponse response = new ConsumerOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerOrder> consumerOrderList = consumerOrderRepository.findByFarmerProduct_ProductID(productID);

            if (consumerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerOrderDto> consumerOrderDtoList = consumerOrderList.stream()
                    .map(consumerOrder -> {
                        ConsumerOrderDto dto = new ConsumerOrderDto();
                        dto.setOrderID(consumerOrder.getOrderID());
                        dto.setProductName(consumerOrder.getProductName());
                        dto.setPrice(consumerOrder.getPrice());
                        dto.setRequiredQuantity(consumerOrder.getRequiredQuantity());
                        dto.setRejected(consumerOrder.isRejected());
                        dto.setAddedDate(consumerOrder.getAddedDate());
                        dto.setDescription(consumerOrder.getDescription());
                        dto.setActive(consumerOrder.isActive());
                        dto.setConfirmed(consumerOrder.isConfirmed());
                        dto.setAddedToCart(consumerOrder.isAddedToCart());
                        dto.setRemovedFromCart(consumerOrder.isRemovedFromCart());
                        dto.setProductCategory(String.valueOf(consumerOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerOrder.getUser().getUserID());
                        userDto.setUserEmail(consumerOrder.getUser().getUserEmail());
                        userDto.setFirstName(consumerOrder.getUser().getFirstName());
                        userDto.setLastName(consumerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(consumerOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(consumerOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(consumerOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(consumerOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(consumerOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(consumerOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(consumerOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(consumerOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(consumerOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(consumerOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(consumerOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(consumerOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(consumerOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(consumerOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(consumerOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(consumerOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerOrderGetResponse(consumerOrderDtoList);
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
    public ConsumerOrderDeleteResponse DeleteConsumerOrderResponse(int orderID) {
        ConsumerOrderDeleteResponse response = new ConsumerOrderDeleteResponse();

        //calculation part
        ConsumerOrder consumerOrder;
        consumerOrder = consumerOrderRepository.findByOrderID(orderID);



        try {
            consumerOrder.setActive(false);
            consumerOrderRepository.save(consumerOrder);
            response.setConsumerOrderDeleteResponse(consumerOrder);
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
    public ConsumerOrderConfirmResponse ConfirmConsumerOrderResponse(int orderID) {
        ConsumerOrderConfirmResponse response = new ConsumerOrderConfirmResponse();

        //calculation part
        ConsumerOrder consumerOrder;
        consumerOrder = consumerOrderRepository.findByOrderID(orderID);



        try {
            consumerOrder.setConfirmed(true);
            consumerOrderRepository.save(consumerOrder);
            response.setConsumerOrderConfirmResponse(consumerOrder);
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
    public ConsumerOrderRejectResponse RejectConsumerOrderResponse(int orderID) {
        ConsumerOrderRejectResponse response = new ConsumerOrderRejectResponse();

        //calculation part
        ConsumerOrder consumerOrder;
        consumerOrder = consumerOrderRepository.findByOrderID(orderID);



        try {
            consumerOrder.setRejected(true);
            consumerOrderRepository.save(consumerOrder);
            response.setConsumerOrderRejectResponse(consumerOrder);
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
    public ConsumerOrderAddToCartResponse AddToCartConsumerOrderResponse(int orderID) {
        ConsumerOrderAddToCartResponse response = new ConsumerOrderAddToCartResponse();

        //calculation part
        ConsumerOrder consumerOrder;
        consumerOrder = consumerOrderRepository.findByOrderID(orderID);



        try {
            FarmerProduct farmerProduct = consumerOrder.getFarmerProduct();
            double requiredQuantity = consumerOrder.getRequiredQuantity();
            double availableQuantity = farmerProduct.getAvailableQuantity();
            if (availableQuantity >= requiredQuantity) {
                // Update the available quantity
                double newAvailableQuantity = availableQuantity - requiredQuantity;
                farmerProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

                // Save the updated product
                farmerProductRepository.save(farmerProduct); // Assuming you have a productRepository

                // Mark order as added to cart
                consumerOrder.setAddedToCart(true);
                consumerOrderRepository.save(consumerOrder);
            response.setConsumerOrderAddToCartResponse(consumerOrder);
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
    public ConsumerOrderRemovedFromCartResponse RemovedFromCartConsumerOrderResponse(int orderID) {
        ConsumerOrderRemovedFromCartResponse response = new ConsumerOrderRemovedFromCartResponse();

        //calculation part
        ConsumerOrder consumerOrder;
        consumerOrder = consumerOrderRepository.findByOrderID(orderID);



        try {
            FarmerProduct farmerProduct = consumerOrder.getFarmerProduct();
            double requiredQuantity = consumerOrder.getRequiredQuantity();
            double availableQuantity = farmerProduct.getAvailableQuantity();
            // Update the available quantity
            double newAvailableQuantity = availableQuantity + requiredQuantity;
            farmerProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

            // Save the updated product
            farmerProductRepository.save(farmerProduct); // Assuming you have a productRepository

            // Mark order as added to cart
            consumerOrder.setRemovedFromCart(true);
            consumerOrderRepository.save(consumerOrder);
            response.setConsumerOrderRemovedFromCartResponse(consumerOrder);
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
