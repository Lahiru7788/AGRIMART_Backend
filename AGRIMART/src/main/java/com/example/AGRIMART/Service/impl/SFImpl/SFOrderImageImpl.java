package com.example.AGRIMART.Service.impl.SFImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrderImage;
import com.example.AGRIMART.Entity.SFEntity.SFOrder;
import com.example.AGRIMART.Entity.SFEntity.SFOrderImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderImageRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.SFRepository.SFOrderImageRepository;
import com.example.AGRIMART.Repository.SFRepository.SFOrderRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderImageService;
import com.example.AGRIMART.Service.SFService.SFOrderImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SFOrderImageImpl implements SFOrderImageService {

    @Autowired
    private SFOrderImageRepository sfOrderImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SFOrderRepository sfOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SFOrderImageAddResponse save(SFOrderImageDto sfOrderImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            SFOrderImageAddResponse response = new SFOrderImageAddResponse();
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
        Optional<SFOrder> orderOptional = sfOrderRepository.findByProductName(productName);


        if (userOptional.isEmpty()) {
            SFOrderImageAddResponse response = new SFOrderImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            SFOrderImageAddResponse response = new SFOrderImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        SFOrder sfOrder = orderOptional.get();

        Optional<SFOrderImage> existingImageOptional = sfOrderImageRepository.findBySFOrder_OrderID(sfOrder.getOrderID());

        SFOrderImage sfOrderImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            sfOrderImage = existingImageOptional.get();
            sfOrderImage.setProductImage(sfOrderImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            sfOrderImage = new SFOrderImage();
            sfOrderImage.setProductImage(sfOrderImageDto.getProductImage());
            sfOrderImage.setUser(user);
            sfOrderImage.setSFOrder(sfOrder);
            actionPerformed = "added";
        }
        SFOrderImageAddResponse response = new SFOrderImageAddResponse();
        try {
            SFOrderImage saveOrderImage = sfOrderImageRepository.save(sfOrderImage);
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
    public SFOrderImageGetResponse GetSFOrderImageFindById(int orderID) {
        SFOrderImageGetResponse response = new SFOrderImageGetResponse();
        try {
            Optional<SFOrderImage> sfOrderImageOptional = sfOrderImageRepository.findById(orderID);

            if (sfOrderImageOptional.isPresent()) {
                SFOrderImage sfOrderImage = sfOrderImageOptional.get();
                SFOrderImageDto dto = new SFOrderImageDto();
                dto.setImageID(sfOrderImage.getImageID());
                dto.setProductImage(sfOrderImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(sfOrderImage.getUser().getUserID());
                userDto.setUserEmail(sfOrderImage.getUser().getUserEmail());
                userDto.setFirstName(sfOrderImage.getUser().getFirstName());
                userDto.setLastName(sfOrderImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(sfOrderImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setSfOrderImageGetResponse(List.of(dto));
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
