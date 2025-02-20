package com.example.AGRIMART.Service.impl.SeedsAndFertilizerImpl;


import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SeedsAndFetilizerProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SeedsAndFertilizerProductAddResponse;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SeedsAndFertilizerProductGetResponse;
import com.example.AGRIMART.Entity.SeedsAndFertilizerEntity.SeedsAndFertilizerProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.SeedsAndFertilizerRepository.SeedsAndFertilizerProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SeedsAndFertilizerService.SeedsAndFertilizerProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeedsAndFertilizerProductImpl implements SeedsAndFertilizerProductService {

    @Autowired
    private SeedsAndFertilizerProductRepository seedsAndFertilizerProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SeedsAndFertilizerProductAddResponse saveOrUpdate(SeedsAndFetilizerProductDto seedsAndFetilizerProductDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            SeedsAndFertilizerProductAddResponse response = new SeedsAndFertilizerProductAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            SeedsAndFertilizerProductAddResponse response = new SeedsAndFertilizerProductAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<SeedsAndFertilizerProduct> existingProductOptional = seedsAndFertilizerProductRepository.findById(seedsAndFetilizerProductDto.getProductID());

        SeedsAndFertilizerProduct seedsAndFertilizerProduct = existingProductOptional.orElse(new SeedsAndFertilizerProduct());
        // If product exists, we update the fields; if not, we create a new one
        seedsAndFertilizerProduct.setProductName(seedsAndFetilizerProductDto.getProductName());
        seedsAndFertilizerProduct.setProductImage(seedsAndFetilizerProductDto.getProductImage());
        seedsAndFertilizerProduct.setPrice(seedsAndFetilizerProductDto.getPrice());
        seedsAndFertilizerProduct.setDescription(seedsAndFetilizerProductDto.getDescription());
        seedsAndFertilizerProduct.setAvailableQuantity(seedsAndFetilizerProductDto.getAvailableQuantity());
        seedsAndFertilizerProduct.setMinimumQuantity(seedsAndFetilizerProductDto.getMinimumQuantity());
        seedsAndFertilizerProduct.setAddedDate(seedsAndFetilizerProductDto.getAddedDate());
        seedsAndFertilizerProduct.setProductCategory(SeedsAndFetilizerProductDto.ProductCategory.valueOf(seedsAndFetilizerProductDto.getProductCategory()));

        // Ensure the user is assigned
        seedsAndFertilizerProduct.setUser(user);

        // Set the flags based on business rules
        seedsAndFertilizerProduct.setActive(true);
        seedsAndFertilizerProduct.setDeleted(false);
        seedsAndFertilizerProduct.setQuantityLowered(false);

        SeedsAndFertilizerProductAddResponse response = new SeedsAndFertilizerProductAddResponse();
        try {
            // Save or update the product
            SeedsAndFertilizerProduct savedProduct = seedsAndFertilizerProductRepository.save(seedsAndFertilizerProduct);

            if (savedProduct != null) {
                response.setMessage("Product was saved/updated successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to save/update product.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }


    //    @Override
//    public UserDetailsGetResponse GetAllUserDetails() {
//        UserDetailsGetResponse response = new UserDetailsGetResponse();
//        try {
//            List<UserDetails> userDetails =userDetailsRepository.findAll();
//            response.setUserDetailsGetResponse(userDetails);
//            response.setStatus("200");
//            response.setMessage("User Details retrieved successfully");
//            response.setResponseCode("1600");
//
//        }catch (Exception e){
//            response.setStatus("500");
//            response.setMessage("Error retrieving User Details: " + e.getMessage());
//            response.setResponseCode("1601");
//
//        }
//
//        return response;
//    }
    public SeedsAndFertilizerProductGetResponse GetAllSeedsAndFertilizerProducts() {
        SeedsAndFertilizerProductGetResponse response = new SeedsAndFertilizerProductGetResponse();
        try {
            // Fetch all user details
            List<SeedsAndFertilizerProduct> seedsAndFertilizerProductList = seedsAndFertilizerProductRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<SeedsAndFetilizerProductDto> seedsAndFetilizerProductDtoList = seedsAndFertilizerProductList.stream()
                    .map(seedsAndFertilizerProduct -> {
                        SeedsAndFetilizerProductDto dto = new SeedsAndFetilizerProductDto();
                        dto.setProductID(seedsAndFertilizerProduct.getProductID());
                        dto.setProductName(seedsAndFertilizerProduct.getProductName());
                        dto.setProductImage(seedsAndFertilizerProduct.getProductImage());
                        dto.setPrice(seedsAndFertilizerProduct.getPrice());
                        dto.setAvailableQuantity(seedsAndFertilizerProduct.getAvailableQuantity());
                        dto.setMinimumQuantity(seedsAndFertilizerProduct.getMinimumQuantity());
                        dto.setAddedDate(seedsAndFertilizerProduct.getAddedDate());
                        dto.setActive(seedsAndFertilizerProduct.isActive());
                        dto.setDeleted(seedsAndFertilizerProduct.isDeleted());
                        dto.setQuantityLowered(seedsAndFertilizerProduct.isQuantityLowered());
                        dto.setProductCategory(String.valueOf(seedsAndFertilizerProduct.getProductCategory()));


                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(seedsAndFertilizerProduct.getUser().getUserID());
                        userDto.setUserEmail(seedsAndFertilizerProduct.getUser().getUserEmail());
                        userDto.setFirstName(seedsAndFertilizerProduct.getUser().getFirstName());
                        userDto.setLastName(seedsAndFertilizerProduct.getUser().getLastName());
                        userDto.setUserType(String.valueOf(seedsAndFertilizerProduct.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSeedsAndFertilizerProductGetResponse(seedsAndFetilizerProductDtoList);
            response.setStatus("200");
            response.setMessage("Product Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Product Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

}
