package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerOrderImpl implements TrainerOrderService {

    @Autowired
    private TrainerOrderRepository trainerOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerOrderAddResponse saveOrUpdate(TrainerOrderDto trainerOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            TrainerOrderAddResponse response = new TrainerOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductID(trainerOrderDto.getProductID());

        if (userOptional.isEmpty()) {
            TrainerOrderAddResponse response = new TrainerOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            TrainerOrderAddResponse response = new TrainerOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<TrainerOrder> existingOrders = trainerOrderRepository.findById(trainerOrderDto.getOrderID());

        TrainerOrder trainerOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            trainerOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            trainerOrder = new TrainerOrder();
            trainerOrder.setUser(user);
            trainerOrder.setFarmerProduct(farmerProduct);
            actionPerformed = "added";
        }

        // Update fields
        trainerOrder.setProductName(trainerOrderDto.getProductName());
        trainerOrder.setPrice(trainerOrderDto.getPrice());
        trainerOrder.setDescription(trainerOrderDto.getDescription());
        trainerOrder.setRequiredQuantity(trainerOrderDto.getRequiredQuantity());
        trainerOrder.setAddedDate(trainerOrderDto.getAddedDate());
        trainerOrder.setProductCategory(SupermarketOrderDto.ProductCategory.valueOf(trainerOrderDto.getProductCategory()));
        trainerOrder.setActive(true);
        trainerOrder.setUser(user);
        trainerOrder.setFarmerProduct(farmerProduct);
        trainerOrder.setConfirmed(false);
        trainerOrder.setRejected(false);
        trainerOrder.setAddedToCart(false);
        trainerOrder.setRemovedFromCart(false);
        trainerOrder.setPaid(false);

        TrainerOrderAddResponse response = new TrainerOrderAddResponse();
        try {
            // Save or update the offer
            TrainerOrder savedOrder = trainerOrderRepository.save(trainerOrder);

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

    public TrainerOrderGetResponse getTrainerOrderByUserId(int userID) {
        TrainerOrderGetResponse response = new TrainerOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerOrder> trainerOrderList = trainerOrderRepository.findByUser_UserID(userID);

            if (trainerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerOrderDto> trainerOrderDtoList = trainerOrderList.stream()
                    .map(trainerOrder -> {
                        TrainerOrderDto dto = new TrainerOrderDto();
                        dto.setOrderID(trainerOrder.getOrderID());
                        dto.setProductName(trainerOrder.getProductName());
                        dto.setPrice(trainerOrder.getPrice());
                        dto.setRequiredQuantity(trainerOrder.getRequiredQuantity());
                        dto.setRejected(trainerOrder.isRejected());
                        dto.setAddedDate(trainerOrder.getAddedDate());
                        dto.setDescription(trainerOrder.getDescription());
                        dto.setActive(trainerOrder.isActive());
                        dto.setConfirmed(trainerOrder.isConfirmed());
                        dto.setAddedToCart(trainerOrder.isAddedToCart());
                        dto.setRemovedFromCart(trainerOrder.isRemovedFromCart());
                        dto.setPaid(trainerOrder.isPaid());
                        dto.setProductCategory(String.valueOf(trainerOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerOrder.getUser().getUserID());
                        userDto.setUserEmail(trainerOrder.getUser().getUserEmail());
                        userDto.setFirstName(trainerOrder.getUser().getFirstName());
                        userDto.setLastName(trainerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(trainerOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(trainerOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(trainerOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(trainerOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(trainerOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(trainerOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(trainerOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(trainerOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(trainerOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(trainerOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(trainerOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(trainerOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(trainerOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(trainerOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(trainerOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(trainerOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerOrderGetResponse(trainerOrderDtoList);
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

    public TrainerOrderGetResponse getTrainerOrderByFarmerUserId(int farmerUserID) {
        TrainerOrderGetResponse response = new TrainerOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<TrainerOrder> trainerOrderList = trainerOrderRepository.findByFarmerProduct_User_UserID(farmerUserID);

            if (trainerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + farmerUserID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerOrderDto> trainerOrderDtoList = trainerOrderList.stream()
                    .map(trainerOrder -> {
                        TrainerOrderDto dto = new TrainerOrderDto();
                        dto.setOrderID(trainerOrder.getOrderID());
                        dto.setProductName(trainerOrder.getProductName());
                        dto.setPrice(trainerOrder.getPrice());
                        dto.setRequiredQuantity(trainerOrder.getRequiredQuantity());
                        dto.setRejected(trainerOrder.isRejected());
                        dto.setAddedDate(trainerOrder.getAddedDate());
                        dto.setDescription(trainerOrder.getDescription());
                        dto.setActive(trainerOrder.isActive());
                        dto.setConfirmed(trainerOrder.isConfirmed());
                        dto.setAddedToCart(trainerOrder.isAddedToCart());
                        dto.setRemovedFromCart(trainerOrder.isRemovedFromCart());
                        dto.setPaid(trainerOrder.isPaid());
                        dto.setProductCategory(String.valueOf(trainerOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerOrder.getUser().getUserID());
                        userDto.setUserEmail(trainerOrder.getUser().getUserEmail());
                        userDto.setFirstName(trainerOrder.getUser().getFirstName());
                        userDto.setLastName(trainerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(trainerOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(trainerOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(trainerOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(trainerOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(trainerOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(trainerOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(trainerOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(trainerOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(trainerOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(trainerOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(trainerOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(trainerOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(trainerOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(trainerOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(trainerOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(trainerOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerOrderGetResponse(trainerOrderDtoList);
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


    public TrainerOrderGetResponse getTrainerOrdersByProductId(int productID) {
        TrainerOrderGetResponse response = new TrainerOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerOrder> trainerOrderList = trainerOrderRepository.findByFarmerProduct_ProductID(productID);

            if (trainerOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerOrderDto> trainerOrderDtoList = trainerOrderList.stream()
                    .map(trainerOrder -> {
                        TrainerOrderDto dto = new TrainerOrderDto();
                        dto.setOrderID(trainerOrder.getOrderID());
                        dto.setProductName(trainerOrder.getProductName());
                        dto.setPrice(trainerOrder.getPrice());
                        dto.setRequiredQuantity(trainerOrder.getRequiredQuantity());
                        dto.setRejected(trainerOrder.isRejected());
                        dto.setAddedDate(trainerOrder.getAddedDate());
                        dto.setDescription(trainerOrder.getDescription());
                        dto.setActive(trainerOrder.isActive());
                        dto.setConfirmed(trainerOrder.isConfirmed());
                        dto.setAddedToCart(trainerOrder.isAddedToCart());
                        dto.setRemovedFromCart(trainerOrder.isRemovedFromCart());
                        dto.setPaid(trainerOrder.isPaid());
                        dto.setProductCategory(String.valueOf(trainerOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerOrder.getUser().getUserID());
                        userDto.setUserEmail(trainerOrder.getUser().getUserEmail());
                        userDto.setFirstName(trainerOrder.getUser().getFirstName());
                        userDto.setLastName(trainerOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(trainerOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(trainerOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(trainerOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(trainerOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(trainerOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(trainerOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(trainerOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(trainerOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(trainerOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(trainerOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(trainerOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(trainerOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(trainerOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(trainerOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(trainerOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(trainerOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerOrderGetResponse(trainerOrderDtoList);
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
    public TrainerOrderDeleteResponse DeleteTrainerOrderResponse(int orderID) {
        TrainerOrderDeleteResponse response = new TrainerOrderDeleteResponse();

        //calculation part
        TrainerOrder trainerOrder;
        trainerOrder = trainerOrderRepository.findByOrderID(orderID);



        try {
            trainerOrder.setActive(false);
            trainerOrderRepository.save(trainerOrder);
            response.setTrainerOrderDeleteResponse(trainerOrder);
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
    public TrainerOrderPaymentResponse PaymentTrainerOrderResponse(int orderID) {
        TrainerOrderPaymentResponse response = new TrainerOrderPaymentResponse();

        //calculation part
        TrainerOrder trainerOrder;
        trainerOrder = trainerOrderRepository.findByOrderID(orderID);



        try {
            trainerOrder.setPaid(true);
            trainerOrderRepository.save(trainerOrder);
            response.setTrainerOrderPaymentResponse(trainerOrder);
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
    public TrainerOrderConfirmResponse ConfirmTrainerOrderResponse(int orderID) {
        TrainerOrderConfirmResponse response = new TrainerOrderConfirmResponse();

        //calculation part
        TrainerOrder trainerOrder;
        trainerOrder = trainerOrderRepository.findByOrderID(orderID);


        try {
            trainerOrder.setConfirmed(true);
            trainerOrderRepository.save(trainerOrder);
            response.setTrainerOrderConfirmResponse(trainerOrder);
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
    public TrainerOrderRejectResponse RejectTrainerOrderResponse(int orderID) {
        TrainerOrderRejectResponse response = new TrainerOrderRejectResponse();

        //calculation part
        TrainerOrder trainerOrder;
        trainerOrder = trainerOrderRepository.findByOrderID(orderID);



        try {
            trainerOrder.setRejected(true);
            trainerOrderRepository.save(trainerOrder);
            response.setTrainerOrderRejectResponse(trainerOrder);
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
    public TrainerOrderAddToCartResponse AddToCartTrainerOrderResponse(int orderID) {
        TrainerOrderAddToCartResponse response = new TrainerOrderAddToCartResponse();

        //calculation part
        TrainerOrder trainerOrder;
        trainerOrder = trainerOrderRepository.findByOrderID(orderID);



        try {
            FarmerProduct farmerProduct = trainerOrder.getFarmerProduct();
            double requiredQuantity = trainerOrder.getRequiredQuantity();
            double availableQuantity = farmerProduct.getAvailableQuantity();
            if (availableQuantity >= requiredQuantity) {
                // Update the available quantity
                double newAvailableQuantity = availableQuantity - requiredQuantity;
                farmerProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

                // Save the updated product
                farmerProductRepository.save(farmerProduct); // Assuming you have a productRepository

                // Mark order as added to cart
                trainerOrder.setAddedToCart(true);
                trainerOrderRepository.save(trainerOrder);
                response.setTrainerOrderAddToCartResponse(trainerOrder);
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
    public TrainerOrderRemovedFromCartResponse RemovedFromCartTrainerOrderResponse(int orderID) {
        TrainerOrderRemovedFromCartResponse response = new TrainerOrderRemovedFromCartResponse();

        //calculation part
        TrainerOrder trainerOrder;
        trainerOrder = trainerOrderRepository.findByOrderID(orderID);


        try {
            FarmerProduct farmerProduct = trainerOrder.getFarmerProduct();
            double requiredQuantity = trainerOrder.getRequiredQuantity();
            double availableQuantity = farmerProduct.getAvailableQuantity();
            // Update the available quantity
            double newAvailableQuantity = availableQuantity + requiredQuantity;
            farmerProduct.setAvailableQuantity(newAvailableQuantity); // Assuming there's a setAvailableQuantity method

            // Save the updated product
            farmerProductRepository.save(farmerProduct); // Assuming you have a productRepository

            // Mark order as added to cart
            trainerOrder.setRemovedFromCart(true);
            trainerOrderRepository.save(trainerOrder);
            response.setTrainerOrderRemovedFromCartResponse(trainerOrder);
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
