package com.example.AGRIMART.Service.impl.SupermarketImpl;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderImageDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketSeedsOrderImageGetResponse;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrderImage;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrderImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketOrderImageRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketOrderRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketSeedsOrderImageRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketSeedsOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOrderImageService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketSeedsOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupermarketSeedsOrderImageImpl implements SupermarketSeedsOrderImageService {

    @Autowired
    private SupermarketSeedsOrderImageRepository supermarketSeedsOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupermarketSeedsOrderRepository supermarketSeedsOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SupermarketSeedsOrderImageAddResponse save(SupermarketSeedsOrderImageDto supermarketSeedsOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            SupermarketSeedsOrderImageAddResponse response = new SupermarketSeedsOrderImageAddResponse();
            response.setMessage("User is not logged in , Order is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

//        if ( null == productID || productID.isEmpty()) {
//            FProductImageAddResponse response = new FProductImageAddResponse();
//            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
//            response.setStatus("401"); // Unauthorized
//            return response;
//        }
        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SupermarketSeedsOrder> orderOptional = supermarketSeedsOrderRepository.findByProductName(productName);


        if (userOptional.isEmpty()) {
            SupermarketSeedsOrderImageAddResponse response = new SupermarketSeedsOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            SupermarketSeedsOrderImageAddResponse response = new SupermarketSeedsOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        SupermarketSeedsOrder supermarketSeedsOrder = orderOptional.get();

        Optional<SupermarketSeedsOrderImage> existingImageOptional = supermarketSeedsOrderImageRepository.findBySupermarketSeedsOrder_OrderID(supermarketSeedsOrder.getOrderID());

        SupermarketSeedsOrderImage supermarketSeedsOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            supermarketSeedsOrderImage = existingImageOptional.get();
            supermarketSeedsOrderImage.setProductImage(supermarketSeedsOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            supermarketSeedsOrderImage = new SupermarketSeedsOrderImage();
            supermarketSeedsOrderImage.setProductImage(supermarketSeedsOrderImageDto.getProductImage());
            supermarketSeedsOrderImage.setUser(user);
            supermarketSeedsOrderImage.setSupermarketSeedsOrder(supermarketSeedsOrder);
            actionPerformed = "added";
        }
        SupermarketSeedsOrderImageAddResponse response = new SupermarketSeedsOrderImageAddResponse();
        try {
            SupermarketSeedsOrderImage saveOrderImage = supermarketSeedsOrderImageRepository.save(supermarketSeedsOrderImage);
            if (saveOrderImage != null) {
                response.setMessage("Order Image was added successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to add Order Image.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    @Override
    public SupermarketSeedsOrderImageGetResponse GetSupermarketSeedsOrderImageFindById(int orderID) {
        SupermarketSeedsOrderImageGetResponse response = new SupermarketSeedsOrderImageGetResponse();
        try {
            Optional<SupermarketSeedsOrderImage> supermarketSeedsOrderImageOptional = supermarketSeedsOrderImageRepository.findById(orderID);

            if (supermarketSeedsOrderImageOptional.isPresent()) {
                SupermarketSeedsOrderImage supermarketSeedsOrderImage = supermarketSeedsOrderImageOptional.get();
                SupermarketSeedsOrderImageDto dto = new SupermarketSeedsOrderImageDto();
                dto.setImageID(supermarketSeedsOrderImage.getImageID());
                dto.setProductImage(supermarketSeedsOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(supermarketSeedsOrderImage.getUser().getUserID());
                userDto.setUserEmail(supermarketSeedsOrderImage.getUser().getUserEmail());
                userDto.setFirstName(supermarketSeedsOrderImage.getUser().getFirstName());
                userDto.setLastName(supermarketSeedsOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(supermarketSeedsOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setSupermarketSeedsOrderImageGetResponse(List.of(dto));
                response.setStatus("200");
                response.setMessage("Product image retrieved successfully.");
                response.setResponseCode("1602");
            } else {
                response.setStatus("404");
                response.setMessage("Product image not found.");
                response.setResponseCode("1603");
            }
        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Product image: " + e.getMessage());
            response.setResponseCode("1604");
        }
        return response;
    }
}
