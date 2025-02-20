package com.example.AGRIMART.Service.impl.SeedsAndFertilizerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SandFProductImageDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageAddResponse;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SandFProductImageAddResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import com.example.AGRIMART.Entity.SeedsAndFertilizerEntity.SandFProductImage;
import com.example.AGRIMART.Entity.SeedsAndFertilizerEntity.SeedsAndFertilizerProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FProductImageRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.SeedsAndFertilizerRepository.SandFProductImageRepository;
import com.example.AGRIMART.Repository.SeedsAndFertilizerRepository.SeedsAndFertilizerProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SeedsAndFertilizerService.SandFProductImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SandFProductImageImpl implements SandFProductImageService {

    @Autowired
    private SandFProductImageRepository sandFProductImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeedsAndFertilizerProductRepository seedsAndFertilizerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SandFProductImageAddResponse save(SandFProductImageDto sandFProductImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer productID = (Integer) session.getAttribute("productID");

        if (username == null || productID == null || username.isEmpty()) {
            SandFProductImageAddResponse response = new SandFProductImageAddResponse();
            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SeedsAndFertilizerProduct> productOptional = seedsAndFertilizerProductRepository.findByProductID(productID);

        if (userOptional.isEmpty() || productOptional.isEmpty()) {
            SandFProductImageAddResponse response = new SandFProductImageAddResponse();
            response.setMessage("User or Product not found for the given username or product name.");
            return response;
        }

        User user = userOptional.get();
        SeedsAndFertilizerProduct seedsAndFertilizerProduct = productOptional.get();
        SandFProductImage sandFProductImage = new SandFProductImage();
        sandFProductImage.setProductImage(sandFProductImageDto.getProductImage());

        sandFProductImage.setUser(user);
        sandFProductImage.setSeedsAndFertilizerProduct(seedsAndFertilizerProduct);

        SandFProductImageAddResponse response = new SandFProductImageAddResponse();
        try {
            SandFProductImage saveProductImage = sandFProductImageRepository.save(sandFProductImage);
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
}
