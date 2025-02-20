package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FProductImageRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerProductImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerProductImageImpl implements FarmerProductImageService {

    @Autowired
    private FProductImageRepository fProductImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FProductImageAddResponse save(FarmerProductImageDto farmerProductImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FProductImageAddResponse response = new FProductImageAddResponse();
            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
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
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductName(productID);

        if (userOptional.isEmpty()) {
            FProductImageAddResponse response = new FProductImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (productOptional.isEmpty()) {
            FProductImageAddResponse response = new FProductImageAddResponse();
            response.setMessage(" Product not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();
        Optional<FarmerProductImage> existingProductOptional = fProductImageRepository.findById(farmerProductImageDto.getImageID());

        FarmerProductImage farmerProductImage = existingProductOptional.orElse(new FarmerProductImage());


        farmerProductImage.setProductImage(farmerProductImageDto.getProductImage());

        farmerProductImage.setUser(user);
        farmerProductImage.setFarmerProduct(farmerProduct);

        FProductImageAddResponse response = new FProductImageAddResponse();
        try {
            FarmerProductImage saveProductImage = fProductImageRepository.save(farmerProductImage);
            if (saveProductImage != null) {
                response.setMessage("Product Image was added successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to add Product Image.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public FProductImageGetResponse GetAllFarmerProductImages() {
        FProductImageGetResponse response = new FProductImageGetResponse();
        try {
            // Fetch all user details
            List<FarmerProductImage> farmerProductImageList = fProductImageRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<FarmerProductImageDto> farmerProductImageDtoList = farmerProductImageList.stream()
                    .map(farmerProductImage -> {
                        FarmerProductImageDto dto = new FarmerProductImageDto();
                        dto.setImageID(farmerProductImage.getImageID());
                        dto.setProductImage(farmerProductImage.getProductImage());


                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerProductImage.getUser().getUserID());
                        userDto.setUserEmail(farmerProductImage.getUser().getUserEmail());
                        userDto.setFirstName(farmerProductImage.getUser().getFirstName());
                        userDto.setLastName(farmerProductImage.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerProductImage.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFProductImageGetResponse(farmerProductImageDtoList);
            response.setStatus("200");
            response.setMessage("Product images retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving product Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }
}
