package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrderImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.CAddOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumerOrderImageImpl implements ConsumerOrderImageService {

    @Autowired
    private ConsumerOrderImageRepository consumerOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerOrderImageAddResponse save(ConsumerOrderImageDto consumerOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            ConsumerOrderImageAddResponse response = new ConsumerOrderImageAddResponse();
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
        Optional<ConsumerOrder> orderOptional = consumerOrderRepository.findByProductName(productName);


        if (userOptional.isEmpty()) {
            ConsumerOrderImageAddResponse response = new ConsumerOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            ConsumerOrderImageAddResponse response = new ConsumerOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        ConsumerOrder consumerOrder = orderOptional.get();

        Optional<ConsumerOrderImage> existingImageOptional = consumerOrderImageRepository.findByConsumerOrder_OrderID(consumerOrder.getOrderID());

        ConsumerOrderImage consumerOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            consumerOrderImage = existingImageOptional.get();
            consumerOrderImage.setProductImage(consumerOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            consumerOrderImage = new ConsumerOrderImage();
            consumerOrderImage.setProductImage(consumerOrderImageDto.getProductImage());
            consumerOrderImage.setUser(user);
            consumerOrderImage.setConsumerOrder(consumerOrder);
            actionPerformed = "added";
        }
        ConsumerOrderImageAddResponse response = new ConsumerOrderImageAddResponse();
        try {
            ConsumerOrderImage saveOrderImage = consumerOrderImageRepository.save(consumerOrderImage);
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
    public ConsumerOrderImageGetResponse GetConsumerOrderImageFindById(int orderID) {
        ConsumerOrderImageGetResponse response = new ConsumerOrderImageGetResponse();
        try {
            Optional<ConsumerOrderImage> consumerOrderImageOptional = consumerOrderImageRepository.findById(orderID);

            if (consumerOrderImageOptional.isPresent()) {
                ConsumerOrderImage consumerOrderImage = consumerOrderImageOptional.get();
                ConsumerOrderImageDto dto = new ConsumerOrderImageDto();
                dto.setImageID(consumerOrderImage.getImageID());
                dto.setProductImage(consumerOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(consumerOrderImage.getUser().getUserID());
                userDto.setUserEmail(consumerOrderImage.getUser().getUserEmail());
                userDto.setFirstName(consumerOrderImage.getUser().getFirstName());
                userDto.setLastName(consumerOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(consumerOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setConsumerOrderImageGetResponse(List.of(dto));
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
