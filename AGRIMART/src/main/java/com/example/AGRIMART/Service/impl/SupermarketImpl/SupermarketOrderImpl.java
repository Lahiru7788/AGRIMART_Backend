package com.example.AGRIMART.Service.impl.SupermarketImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupermarketOrderImpl implements SupermarketOrderService {

    @Autowired
    private SupermarketOrderRepository supermarketOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SupermarketOrderAddResponse saveOrUpdate(SupermarketOrderDto supermarketOrderDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            SupermarketOrderAddResponse response = new SupermarketOrderAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductName(productID);

        if (userOptional.isEmpty()) {
            SupermarketOrderAddResponse response = new SupermarketOrderAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            SupermarketOrderAddResponse response = new SupermarketOrderAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<SupermarketOrder> existingOrders = supermarketOrderRepository.findById(supermarketOrderDto.getOrderID());

        SupermarketOrder supermarketOrder;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            supermarketOrder = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            supermarketOrder = new SupermarketOrder();
            supermarketOrder.setUser(user);
            supermarketOrder.setFarmerProduct(farmerProduct);
            actionPerformed = "added";
        }

        // Update fields
        supermarketOrder.setProductName(supermarketOrderDto.getProductName());
        supermarketOrder.setPrice(supermarketOrderDto.getPrice());
        supermarketOrder.setDescription(supermarketOrderDto.getDescription());
        supermarketOrder.setRequiredQuantity(supermarketOrderDto.getRequiredQuantity());
        supermarketOrder.setAddedDate(supermarketOrderDto.getAddedDate());
        supermarketOrder.setProductCategory(SupermarketOrderDto.ProductCategory.valueOf(supermarketOrderDto.getProductCategory()));
        supermarketOrder.setActive(true);
        supermarketOrder.setUser(user);
        supermarketOrder.setFarmerProduct(farmerProduct);
        supermarketOrder.setConfirmed(false);
        supermarketOrder.setRejected(false);

        SupermarketOrderAddResponse response = new SupermarketOrderAddResponse();
        try {
            // Save or update the offer
            SupermarketOrder savedOrder = supermarketOrderRepository.save(supermarketOrder);

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

    public SupermarketOrderGetResponse getSupermarketOrderByUserId(int userID) {
        SupermarketOrderGetResponse response = new SupermarketOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SupermarketOrder> supermarketOrderList = supermarketOrderRepository.findByUser_UserID(userID);

            if (supermarketOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SupermarketOrderDto> supermarketOrderDtoList = supermarketOrderList.stream()
                    .map(supermarketOrder -> {
                        SupermarketOrderDto dto = new SupermarketOrderDto();
                        dto.setOrderID(supermarketOrder.getOrderID());
                        dto.setProductName(supermarketOrder.getProductName());
                        dto.setPrice(supermarketOrder.getPrice());
                        dto.setRequiredQuantity(supermarketOrder.getRequiredQuantity());
                        dto.setRejected(supermarketOrder.isRejected());
                        dto.setAddedDate(supermarketOrder.getAddedDate());
                        dto.setDescription(supermarketOrder.getDescription());
                        dto.setActive(supermarketOrder.isActive());
                        dto.setConfirmed(supermarketOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(supermarketOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(supermarketOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(supermarketOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(supermarketOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(supermarketOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(supermarketOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(supermarketOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(supermarketOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(supermarketOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(supermarketOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(supermarketOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(supermarketOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(supermarketOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(supermarketOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(supermarketOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(supermarketOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketOrderGetResponse(supermarketOrderDtoList);
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

    public SupermarketOrderGetResponse getSupermarketOrderByFarmerUserId(int farmerUserID) {
        SupermarketOrderGetResponse response = new SupermarketOrderGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<SupermarketOrder> supermarketOrderList = supermarketOrderRepository.findByFarmerProduct_User_UserID(farmerUserID);

            if (supermarketOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + farmerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<SupermarketOrderDto> supermarketOrderDtoList = supermarketOrderList.stream()
                    .map(supermarketOrder -> {
                        SupermarketOrderDto dto = new SupermarketOrderDto();
                        dto.setOrderID(supermarketOrder.getOrderID());
                        dto.setProductName(supermarketOrder.getProductName());
                        dto.setPrice(supermarketOrder.getPrice());
                        dto.setRequiredQuantity(supermarketOrder.getRequiredQuantity());
                        dto.setRejected(supermarketOrder.isRejected());
                        dto.setAddedDate(supermarketOrder.getAddedDate());
                        dto.setDescription(supermarketOrder.getDescription());
                        dto.setActive(supermarketOrder.isActive());
                        dto.setConfirmed(supermarketOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(supermarketOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(supermarketOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(supermarketOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(supermarketOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(supermarketOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(supermarketOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(supermarketOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(supermarketOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(supermarketOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(supermarketOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(supermarketOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(supermarketOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(supermarketOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(supermarketOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(supermarketOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(supermarketOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketOrderGetResponse(supermarketOrderDtoList);
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


    public SupermarketOrderGetResponse getSupermarketOrdersByProductId(int productID) {
        SupermarketOrderGetResponse response = new SupermarketOrderGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SupermarketOrder> supermarketOrderList = supermarketOrderRepository.findByFarmerProduct_ProductID(productID);

            if (supermarketOrderList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SupermarketOrderDto> supermarketOrderDtoList = supermarketOrderList.stream()
                    .map(supermarketOrder -> {
                        SupermarketOrderDto dto = new SupermarketOrderDto();
                        dto.setOrderID(supermarketOrder.getOrderID());
                        dto.setProductName(supermarketOrder.getProductName());
                        dto.setPrice(supermarketOrder.getPrice());
                        dto.setRequiredQuantity(supermarketOrder.getRequiredQuantity());
                        dto.setRejected(supermarketOrder.isRejected());
                        dto.setAddedDate(supermarketOrder.getAddedDate());
                        dto.setDescription(supermarketOrder.getDescription());
                        dto.setActive(supermarketOrder.isActive());
                        dto.setConfirmed(supermarketOrder.isConfirmed());
                        dto.setProductCategory(String.valueOf(supermarketOrder.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketOrder.getUser().getUserID());
                        userDto.setUserEmail(supermarketOrder.getUser().getUserEmail());
                        userDto.setFirstName(supermarketOrder.getUser().getFirstName());
                        userDto.setLastName(supermarketOrder.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketOrder.getUser().getUserType()));
                        dto.setUser(userDto);

                        FarmerProductDto farmerProductDto = new FarmerProductDto();
                        farmerProductDto.setProductID(supermarketOrder.getFarmerProduct().getProductID());
                        farmerProductDto.setProductName(supermarketOrder.getFarmerProduct().getProductName());
                        farmerProductDto.setPrice(supermarketOrder.getFarmerProduct().getPrice());
                        farmerProductDto.setAvailableQuantity(supermarketOrder.getFarmerProduct().getAvailableQuantity());
                        farmerProductDto.setMinimumQuantity(supermarketOrder.getFarmerProduct().getMinimumQuantity());
                        farmerProductDto.setAddedDate(supermarketOrder.getFarmerProduct().getAddedDate());
                        farmerProductDto.setDescription(supermarketOrder.getFarmerProduct().getDescription());
                        farmerProductDto.setActive(supermarketOrder.getFarmerProduct().isActive());
                        farmerProductDto.setDeleted(supermarketOrder.getFarmerProduct().isDeleted());
                        farmerProductDto.setQuantityLowered(supermarketOrder.getFarmerProduct().isQuantityLowered());
                        farmerProductDto.setProductCategory(String.valueOf(supermarketOrder.getFarmerProduct().getProductCategory()));

                        UserDto farmerUserDto = new UserDto();
                        farmerUserDto.setUserID(supermarketOrder.getFarmerProduct().getUser().getUserID());
                        farmerUserDto.setUserEmail(supermarketOrder.getFarmerProduct().getUser().getUserEmail());
                        farmerUserDto.setFirstName(supermarketOrder.getFarmerProduct().getUser().getFirstName());
                        farmerUserDto.setLastName(supermarketOrder.getFarmerProduct().getUser().getLastName());
                        farmerUserDto.setUserType(String.valueOf(supermarketOrder.getFarmerProduct().getUser().getUserType()));
                        farmerProductDto.setUser(farmerUserDto);

                        dto.setFarmerProduct(farmerProductDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketOrderGetResponse(supermarketOrderDtoList);
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
    public SupermarketOrderDeleteResponse DeleteSupermarketOrderResponse(int orderID) {
        SupermarketOrderDeleteResponse response = new SupermarketOrderDeleteResponse();

        //calculation part
        SupermarketOrder supermarketOrder;
        supermarketOrder = supermarketOrderRepository.findByOrderID(orderID);



        try {
            supermarketOrder.setActive(false);
            supermarketOrderRepository.save(supermarketOrder);
            response.setSupermarketOrderDeleteResponse(supermarketOrder);
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
    public SupermarketOrderConfirmResponse ConfirmSupermarketOrderResponse(int orderID) {
        SupermarketOrderConfirmResponse response = new SupermarketOrderConfirmResponse();

        //calculation part
        SupermarketOrder supermarketOrder;
        supermarketOrder = supermarketOrderRepository.findByOrderID(orderID);



        try {
            supermarketOrder.setConfirmed(true);
            supermarketOrderRepository.save(supermarketOrder);
            response.setSupermarketOrderConfirmResponse(supermarketOrder);
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
    public SupermarketOrderRejectResponse RejectSupermarketOrderResponse(int orderID) {
        SupermarketOrderRejectResponse response = new SupermarketOrderRejectResponse();

        //calculation part
        SupermarketOrder supermarketOrder;
        supermarketOrder = supermarketOrderRepository.findByOrderID(orderID);



        try {
            supermarketOrder.setRejected(true);
            supermarketOrderRepository.save(supermarketOrder);
            response.setSupermarketOrderRejectResponse(supermarketOrder);
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
