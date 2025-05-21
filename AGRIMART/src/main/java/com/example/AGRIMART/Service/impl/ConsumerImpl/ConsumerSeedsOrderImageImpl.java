package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrderImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerSeedsOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumerSeedsOrderImageImpl implements ConsumerSeedsOrderImageService {

    @Autowired
    private ConsumerSeedsOrderImageRepository consumerSeedsOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsumerSeedsOrderRepository consumerSeedsOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerSeedsOrderImageAddResponse save(ConsumerSeedsOrderImageDto consumerSeedsOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            ConsumerSeedsOrderImageAddResponse response = new ConsumerSeedsOrderImageAddResponse();
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
        Optional<ConsumerSeedsOrder> orderOptional = consumerSeedsOrderRepository.findByProductName(productName);


        if (userOptional.isEmpty()) {
            ConsumerSeedsOrderImageAddResponse response = new ConsumerSeedsOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            ConsumerSeedsOrderImageAddResponse response = new ConsumerSeedsOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        ConsumerSeedsOrder consumerSeedsOrder = orderOptional.get();

        Optional<ConsumerSeedsOrderImage> existingImageOptional = consumerSeedsOrderImageRepository.findByConsumerSeedsOrder_OrderID(consumerSeedsOrder.getOrderID());

        ConsumerSeedsOrderImage consumerSeedsOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            consumerSeedsOrderImage = existingImageOptional.get();
            consumerSeedsOrderImage.setProductImage(consumerSeedsOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            consumerSeedsOrderImage = new ConsumerSeedsOrderImage();
            consumerSeedsOrderImage.setProductImage(consumerSeedsOrderImageDto.getProductImage());
            consumerSeedsOrderImage.setUser(user);
            consumerSeedsOrderImage.setConsumerSeedsOrder(consumerSeedsOrder);
            actionPerformed = "added";
        }
        ConsumerSeedsOrderImageAddResponse response = new ConsumerSeedsOrderImageAddResponse();
        try {
            ConsumerSeedsOrderImage saveOrderImage = consumerSeedsOrderImageRepository.save(consumerSeedsOrderImage);
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
    public ConsumerSeedsOrderImageGetResponse GetConsumerSeedsOrderImageFindById(int orderID) {
        ConsumerSeedsOrderImageGetResponse response = new ConsumerSeedsOrderImageGetResponse();
        try {
            Optional<ConsumerSeedsOrderImage> consumerSeedsOrderImageOptional = consumerSeedsOrderImageRepository.findById(orderID);

            if (consumerSeedsOrderImageOptional.isPresent()) {
                ConsumerSeedsOrderImage consumerSeedsOrderImage = consumerSeedsOrderImageOptional.get();
                ConsumerSeedsOrderImageDto dto = new ConsumerSeedsOrderImageDto();
                dto.setImageID(consumerSeedsOrderImage.getImageID());
                dto.setProductImage(consumerSeedsOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(consumerSeedsOrderImage.getUser().getUserID());
                userDto.setUserEmail(consumerSeedsOrderImage.getUser().getUserEmail());
                userDto.setFirstName(consumerSeedsOrderImage.getUser().getFirstName());
                userDto.setLastName(consumerSeedsOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(consumerSeedsOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setConsumerSeedsOrderImageGetResponse(List.of(dto));
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
