package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrderImage;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrderImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerSeedsOrderImageRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerSeedsOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerSeedsOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerSeedsOrderImageImpl implements FarmerSeedsOrderImageService {

    @Autowired
    private FarmerSeedsOrderImageRepository farmerSeedsOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerSeedsOrderRepository farmerSeedsOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerSeedsOrderImageAddResponse save(FarmerSeedsOrderImageDto farmerSeedsOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            FarmerSeedsOrderImageAddResponse response = new FarmerSeedsOrderImageAddResponse();
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
        Optional<FarmerSeedsOrder> orderOptional = farmerSeedsOrderRepository.findByProductName(productName);


        if (userOptional.isEmpty()) {
            FarmerSeedsOrderImageAddResponse response = new FarmerSeedsOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            FarmerSeedsOrderImageAddResponse response = new FarmerSeedsOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        FarmerSeedsOrder farmerSeedsOrder = orderOptional.get();

        Optional<FarmerSeedsOrderImage> existingImageOptional = farmerSeedsOrderImageRepository.findByFarmerSeedsOrder_OrderID(farmerSeedsOrder.getOrderID());

        FarmerSeedsOrderImage farmerSeedsOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            farmerSeedsOrderImage = existingImageOptional.get();
            farmerSeedsOrderImage.setProductImage(farmerSeedsOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            farmerSeedsOrderImage = new FarmerSeedsOrderImage();
            farmerSeedsOrderImage.setProductImage(farmerSeedsOrderImageDto.getProductImage());
            farmerSeedsOrderImage.setUser(user);
            farmerSeedsOrderImage.setFarmerSeedsOrder(farmerSeedsOrder);
            actionPerformed = "added";
        }
        FarmerSeedsOrderImageAddResponse response = new FarmerSeedsOrderImageAddResponse();
        try {
            FarmerSeedsOrderImage saveOrderImage = farmerSeedsOrderImageRepository.save(farmerSeedsOrderImage);
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
    public FarmerSeedsOrderImageGetResponse GetFarmerSeedsOrderImageFindById(int orderID) {
        FarmerSeedsOrderImageGetResponse response = new FarmerSeedsOrderImageGetResponse();
        try {
            Optional<FarmerSeedsOrderImage> farmerSeedsOrderImageOptional = farmerSeedsOrderImageRepository.findById(orderID);

            if (farmerSeedsOrderImageOptional.isPresent()) {
                FarmerSeedsOrderImage farmerSeedsOrderImage = farmerSeedsOrderImageOptional.get();
                FarmerSeedsOrderImageDto dto = new FarmerSeedsOrderImageDto();
                dto.setImageID(farmerSeedsOrderImage.getImageID());
                dto.setProductImage(farmerSeedsOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(farmerSeedsOrderImage.getUser().getUserID());
                userDto.setUserEmail(farmerSeedsOrderImage.getUser().getUserEmail());
                userDto.setFirstName(farmerSeedsOrderImage.getUser().getFirstName());
                userDto.setLastName(farmerSeedsOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(farmerSeedsOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setFarmerSeedsOrderImageGetResponse(List.of(dto));
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
