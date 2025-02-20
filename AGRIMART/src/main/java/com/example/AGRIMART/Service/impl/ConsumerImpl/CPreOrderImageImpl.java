package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.ConsumerDto.CPreOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CPreOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CPreOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.CPreOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerPreOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.CAddOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.CPreOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerPreOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.CPreOrderImageService;
import com.example.AGRIMART.Service.FarmerService.FarmerProductImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CPreOrderImageImpl implements CPreOrderImageService {

    @Autowired
    private CPreOrderImageRepository cPreOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsumerPreOrderRepository consumerPreOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public CPreOrderImageAddResponse save(CPreOrderImageDto cPreOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            CPreOrderImageAddResponse response = new CPreOrderImageAddResponse();
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
        Optional<ConsumerPreOrder> preOrderOptional = consumerPreOrderRepository.findByProductName(productName);

        if (userOptional.isEmpty()) {
            CPreOrderImageAddResponse response = new CPreOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (preOrderOptional.isEmpty()) {
            CPreOrderImageAddResponse response = new CPreOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        ConsumerPreOrder consumerPreOrder = preOrderOptional.get();
        Optional<CPreOrderImage> existingPreOrderOptional = cPreOrderImageRepository.findById(cPreOrderImageDto.getImageID());

        CPreOrderImage cPreOrderImage = existingPreOrderOptional.orElse(new CPreOrderImage());


        cPreOrderImage.setProductImage(cPreOrderImageDto.getProductImage());

        cPreOrderImage.setUser(user);
        cPreOrderImage.setConsumerPreOrder(consumerPreOrder);

        CPreOrderImageAddResponse response = new CPreOrderImageAddResponse();
        try {
            CPreOrderImage saveOrderImage = cPreOrderImageRepository.save(cPreOrderImage);
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

    public CPreOrderImageGetResponse GetAllConsumerPreOrderImages() {
        CPreOrderImageGetResponse response = new CPreOrderImageGetResponse();
        try {
            // Fetch all user details
            List<CPreOrderImage> cPreOrderImageList = cPreOrderImageRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<CPreOrderImageDto> cPreOrderImageDtoList = cPreOrderImageList.stream()
                    .map(cPreOrderImage -> {
                        CPreOrderImageDto dto = new CPreOrderImageDto();
                        dto.setImageID(cPreOrderImage.getImageID());
                        dto.setProductImage(cPreOrderImage.getProductImage());


                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(cPreOrderImage.getUser().getUserID());
                        userDto.setUserEmail(cPreOrderImage.getUser().getUserEmail());
                        userDto.setFirstName(cPreOrderImage.getUser().getFirstName());
                        userDto.setLastName(cPreOrderImage.getUser().getLastName());
                        userDto.setUserType(String.valueOf(cPreOrderImage.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setCPreOrderGetResponse(cPreOrderImageDtoList);
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
