package com.example.AGRIMART.Service.impl.SupermarketImpl;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.SupermarketDto.SAddOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SAddOrderImage;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.CAddOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SAddOrderImageRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketAddOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.CAddOrderImageService;
import com.example.AGRIMART.Service.SupermarketService.SAddOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SAddOrderImageImpl implements SAddOrderImageService {

    @Autowired
    private SAddOrderImageRepository sAddOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupermarketAddOrderRepository supermarketAddOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SAddOrderImageAddResponse save(SAddOrderImageDto sAddOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer orderID = (Integer) session.getAttribute("orderID");

        if (username == null || username.isEmpty()) {
            SAddOrderImageAddResponse response = new SAddOrderImageAddResponse();
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
        Optional<SupermarketAddOrder> addOrderOptional = supermarketAddOrderRepository.findById(orderID);


        if (userOptional.isEmpty()) {
            SAddOrderImageAddResponse response = new SAddOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (addOrderOptional.isEmpty()) {
            SAddOrderImageAddResponse response = new SAddOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        SupermarketAddOrder supermarketAddOrder = addOrderOptional.get();

        Optional<SAddOrderImage> existingImageOptional = sAddOrderImageRepository.findBySupermarketAddOrder_OrderID(supermarketAddOrder.getOrderID());

        SAddOrderImage sAddOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            sAddOrderImage = existingImageOptional.get();
            sAddOrderImage.setProductImage(sAddOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            sAddOrderImage = new SAddOrderImage();
            sAddOrderImage.setProductImage(sAddOrderImageDto.getProductImage());
            sAddOrderImage.setUser(user);
            sAddOrderImage.setSupermarketAddOrder(supermarketAddOrder);
            actionPerformed = "added";
        }
        SAddOrderImageAddResponse response = new SAddOrderImageAddResponse();
        try {
            SAddOrderImage saveOrderImage = sAddOrderImageRepository.save(sAddOrderImage);
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

//    public CAddOrderImageGetResponse GetAllConsumerAddOrderImages() {
//        CAddOrderImageGetResponse response = new CAddOrderImageGetResponse();
//        try {
//            // Fetch all user details
//            List<CAddOrderImage> cAddOrderImageList = cAddOrderImageRepository.findAll();
//
//            // Map UserDetails entities to a simplified DTO without sensitive data
//            List<CAddOrderImageDto> cAddOrderImageDtoList = cAddOrderImageList.stream()
//                    .map(cAddOrderImage -> {
//                        CAddOrderImageDto dto = new CAddOrderImageDto();
//                        dto.setImageID(cAddOrderImage.getImageID());
//                        dto.setProductImage(cAddOrderImage.getProductImage());
//
//
//                        // Map nested user information without credentials
//                        UserDto userDto = new UserDto();
//                        userDto.setUserID(cAddOrderImage.getUser().getUserID());
//                        userDto.setUserEmail(cAddOrderImage.getUser().getUserEmail());
//                        userDto.setFirstName(cAddOrderImage.getUser().getFirstName());
//                        userDto.setLastName(cAddOrderImage.getUser().getLastName());
//                        userDto.setUserType(String.valueOf(cAddOrderImage.getUser().getUserType()));
//
//
//                        dto.setUser(userDto);
//                        return dto;
//                    })
//                    .collect(Collectors.toList());
//
//            response.setCAddOrderImageGetResponse(cAddOrderImageDtoList);
//            response.setStatus("200");
//            response.setMessage("Order images retrieved successfully");
//            response.setResponseCode("1600");
//
//        } catch (Exception e) {
//            response.setStatus("500");
//            response.setMessage("Error retrieving Order Images: " + e.getMessage());
//            response.setResponseCode("1601");
//        }
//
//        return response;
//    }

    @Override
    public SAddOrderImageGetResponse GetSAddOrderImageFindById(int orderID) {
        SAddOrderImageGetResponse response = new SAddOrderImageGetResponse();
        try {
            Optional<SAddOrderImage> sAddOrderImageOptional = sAddOrderImageRepository.findById(orderID);

            if (sAddOrderImageOptional.isPresent()) {
                SAddOrderImage sAddOrderImage = sAddOrderImageOptional.get();
                SAddOrderImageDto dto = new SAddOrderImageDto();
                dto.setImageID(sAddOrderImage.getImageID());
                dto.setProductImage(sAddOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(sAddOrderImage.getUser().getUserID());
                userDto.setUserEmail(sAddOrderImage.getUser().getUserEmail());
                userDto.setFirstName(sAddOrderImage.getUser().getFirstName());
                userDto.setLastName(sAddOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(sAddOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setSAddOrderImageGetResponse(List.of(dto));
                response.setStatus("200");
                response.setMessage("Profile image retrieved successfully.");
                response.setResponseCode("1602");
            } else {
                response.setStatus("404");
                response.setMessage("Profile image not found.");
                response.setResponseCode("1603");
            }
        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Profile image: " + e.getMessage());
            response.setResponseCode("1604");
        }
        return response;
    }
}
