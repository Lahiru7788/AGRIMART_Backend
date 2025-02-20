package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.CAddOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.CAddOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CAddOrderImageImpl implements CAddOrderImageService {

    @Autowired
    private CAddOrderImageRepository cAddOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsumerAddOrderRepository consumerAddOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public CAddOrderImageAddResponse save(CAddOrderImageDto cAddOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            CAddOrderImageAddResponse response = new CAddOrderImageAddResponse();
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
        Optional<ConsumerAddOrder> addOrderOptional = consumerAddOrderRepository.findByProductName(productName);

        if (userOptional.isEmpty()) {
            CAddOrderImageAddResponse response = new CAddOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (addOrderOptional.isEmpty()) {
            CAddOrderImageAddResponse response = new CAddOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        ConsumerAddOrder consumerAddOrder = addOrderOptional.get();
        Optional<CAddOrderImage> existingAddOrderOptional = cAddOrderImageRepository.findById(cAddOrderImageDto.getImageID());

        CAddOrderImage cAddOrderImage = existingAddOrderOptional.orElse(new CAddOrderImage());


        cAddOrderImage.setProductImage(cAddOrderImageDto.getProductImage());

        cAddOrderImage.setUser(user);
        cAddOrderImage.setConsumerAddOrder(consumerAddOrder);

        CAddOrderImageAddResponse response = new CAddOrderImageAddResponse();
        try {
            CAddOrderImage saveOrderImage = cAddOrderImageRepository.save(cAddOrderImage);
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

    public CAddOrderImageGetResponse GetAllConsumerAddOrderImages() {
        CAddOrderImageGetResponse response = new CAddOrderImageGetResponse();
        try {
            // Fetch all user details
            List<CAddOrderImage> cAddOrderImageList = cAddOrderImageRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<CAddOrderImageDto> cAddOrderImageDtoList = cAddOrderImageList.stream()
                    .map(cAddOrderImage -> {
                        CAddOrderImageDto dto = new CAddOrderImageDto();
                        dto.setImageID(cAddOrderImage.getImageID());
                        dto.setProductImage(cAddOrderImage.getProductImage());


                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(cAddOrderImage.getUser().getUserID());
                        userDto.setUserEmail(cAddOrderImage.getUser().getUserEmail());
                        userDto.setFirstName(cAddOrderImage.getUser().getFirstName());
                        userDto.setLastName(cAddOrderImage.getUser().getLastName());
                        userDto.setUserType(String.valueOf(cAddOrderImage.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setCAddOrderImageGetResponse(cAddOrderImageDtoList);
            response.setStatus("200");
            response.setMessage("Order images retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Order Images: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }
}
