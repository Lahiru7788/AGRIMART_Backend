package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerOrderImageGetResponse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrderImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerOrderImageRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerOrderImageImpl implements TrainerOrderImageService {

    @Autowired
    private TrainerOrderImageRepository trainerOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerOrderRepository trainerOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerOrderImageAddResponse save(TrainerOrderImageDto trainerOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            TrainerOrderImageAddResponse response = new TrainerOrderImageAddResponse();
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
        Optional<TrainerOrder> orderOptional = trainerOrderRepository.findByProductName(productName);


        if (userOptional.isEmpty()) {
            TrainerOrderImageAddResponse response = new TrainerOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            TrainerOrderImageAddResponse response = new TrainerOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        TrainerOrder trainerOrder = orderOptional.get();

        Optional<TrainerOrderImage> existingImageOptional = trainerOrderImageRepository.findByTrainerOrder_OrderID(trainerOrder.getOrderID());

        TrainerOrderImage trainerOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            trainerOrderImage = existingImageOptional.get();
            trainerOrderImage.setProductImage(trainerOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            trainerOrderImage = new TrainerOrderImage();
            trainerOrderImage.setProductImage(trainerOrderImageDto.getProductImage());
            trainerOrderImage.setUser(user);
            trainerOrderImage.setTrainerOrder(trainerOrder);
            actionPerformed = "added";
        }
        TrainerOrderImageAddResponse response = new TrainerOrderImageAddResponse();
        try {
            TrainerOrderImage saveOrderImage = trainerOrderImageRepository.save(trainerOrderImage);
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
    public TrainerOrderImageGetResponse GetTrainerOrderImageFindById(int orderID) {
        TrainerOrderImageGetResponse response = new TrainerOrderImageGetResponse();
        try {
            Optional<TrainerOrderImage> trainerOrderImageOptional = trainerOrderImageRepository.findById(orderID);

            if (trainerOrderImageOptional.isPresent()) {
                TrainerOrderImage trainerOrderImage = trainerOrderImageOptional.get();
                TrainerOrderImageDto dto = new TrainerOrderImageDto();
                dto.setImageID(trainerOrderImage.getImageID());
                dto.setProductImage(trainerOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(trainerOrderImage.getUser().getUserID());
                userDto.setUserEmail(trainerOrderImage.getUser().getUserEmail());
                userDto.setFirstName(trainerOrderImage.getUser().getFirstName());
                userDto.setLastName(trainerOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(trainerOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setTrainerOrderImageGetResponse(List.of(dto));
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
