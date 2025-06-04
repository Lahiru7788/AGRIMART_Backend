package com.example.AGRIMART.Service.impl.SFImpl;

import com.example.AGRIMART.Dto.SFDto.SFProductImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductImageAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductImageGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SFEntity.SFProductImage;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.SFRepository.SFProductImageRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SFService.SFProductImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SFProductImageImpl implements SFProductImageService {

    @Autowired
    private SFProductImageRepository SFProductImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SFProductRepository SFProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SFProductImageAddResponse save(SFProductImageDto SFProductImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer productID = (Integer) session.getAttribute("productID");

        if (username == null || productID == null || username.isEmpty()) {
            SFProductImageAddResponse response = new SFProductImageAddResponse();
            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SFProduct> productOptional = SFProductRepository.findByProductID(productID);


        if (userOptional.isEmpty() || productOptional.isEmpty()) {
            SFProductImageAddResponse response = new SFProductImageAddResponse();
            response.setMessage("User or Product not found for the given username or product name.");
            return response;
        }

        User user = userOptional.get();
        SFProduct SFProduct = productOptional.get();

        Optional<SFProductImage> existingImageOptional = SFProductImageRepository.findBySFProduct_ProductID(SFProduct.getProductID());

        SFProductImage SFProductImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            SFProductImage = existingImageOptional.get();
            SFProductImage.setProductImage(SFProductImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            // Create new image entry
            SFProductImage = new SFProductImage();
            SFProductImage.setProductImage(SFProductImageDto.getProductImage());
            SFProductImage.setUser(user);
            SFProductImage.setSFProduct(SFProduct);
            actionPerformed = "added";
        }


        SFProductImageAddResponse response = new SFProductImageAddResponse();
        try {
            SFProductImage saveProductImage = SFProductImageRepository.save(SFProductImage);
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



    @Override
    public SFProductImageGetResponse GetSAndFProductImageFindById(int productID) {
        SFProductImageGetResponse response = new SFProductImageGetResponse();
        try {
            Optional<SFProductImage> productImageOptional = SFProductImageRepository.findById(productID);

            if (productImageOptional.isPresent()) {
                SFProductImage productImage = productImageOptional.get();
                SFProductImageDto dto = new SFProductImageDto();
                dto.setImageID(productImage.getImageID());
                dto.setProductImage(productImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(productImage.getUser().getUserID());
                userDto.setUserEmail(productImage.getUser().getUserEmail());
                userDto.setFirstName(productImage.getUser().getFirstName());
                userDto.setLastName(productImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(productImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setSandFProductImageGetResponse(List.of(dto));
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
            response.setMessage("Error retrieving product image: " + e.getMessage());
            response.setResponseCode("1604");
        }
        return response;
    }
}
