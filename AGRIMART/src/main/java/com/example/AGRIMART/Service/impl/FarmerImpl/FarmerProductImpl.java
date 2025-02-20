package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerProductImpl implements FarmerProductService {

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
//    public FarmerProductAddResponse save(FarmerProductDto farmerProductDto) {
//
//        String username = (String) session.getAttribute("userEmail");
//
//        if (username == null || username.isEmpty()) {
//            FarmerProductAddResponse response = new FarmerProductAddResponse();
//            response.setMessage("User is not logged in or session expired.");
//            response.setStatus("401"); // Unauthorized
//            return response;
//        }
//
//        Optional<User> userOptional = userRepository.findByUserEmail(username);
//
//        if (userOptional.isEmpty()) {
//            FarmerProductAddResponse response = new FarmerProductAddResponse();
//            response.setMessage("User not found for the given username.");
//            return response;
//        }
//
//        User user = userOptional.get();
//        FarmerProduct farmerProduct = new FarmerProduct();
//        farmerProduct.setProductName(farmerProductDto.getProductName());
//        farmerProduct.setProductImage(farmerProductDto.getProductImage());
//        farmerProduct.setPrice(farmerProductDto.getPrice());
//        farmerProduct.setDescription(farmerProductDto.getDescription());
//        farmerProduct.setAvailableQuantity(farmerProductDto.getAvailableQuantity());
//        farmerProduct.setMinimumQuantity(farmerProductDto.getMinimumQuantity());
//        farmerProduct.setAddedDate(farmerProductDto.getAddedDate());
//        farmerProduct.setProductCategory(FarmerProductDto.ProductCategory.valueOf(farmerProductDto.getProductCategory()));
//
//        farmerProduct.setUser(user);
//        farmerProduct.setActive(true);
//        farmerProduct.setDeleted(false);
//        farmerProduct.setQuantityLowered(false);
//
//        FarmerProductAddResponse response = new FarmerProductAddResponse();
//        try {
//            FarmerProduct saveFarmerProduct = farmerProductRepository.save(farmerProduct);
//            if (saveFarmerProduct != null) {
//                response.setMessage("Product was added successfully.");
//                response.setStatus("200");
//                response.setResponseCode("1000");
//            } else {
//                response.setMessage("Failed to add product.");
//                response.setStatus("400");
//            }
//        } catch (Exception e) {
//            response.setMessage("Error: " + e.getMessage());
//            response.setStatus("500"); // Internal server error
//        }
//
//        return response;
//    }

    public FarmerProductAddResponse saveOrUpdate(FarmerProductDto farmerProductDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            FarmerProductAddResponse response = new FarmerProductAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            FarmerProductAddResponse response = new FarmerProductAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<FarmerProduct> existingProductOptional = farmerProductRepository.findById(farmerProductDto.getProductID());
        FarmerProduct farmerProduct = existingProductOptional.orElse(new FarmerProduct());
        // If product exists, we update the fields; if not, we create a new one
        farmerProduct.setProductName(farmerProductDto.getProductName());
        farmerProduct.setProductImage(farmerProductDto.getProductImage());
        farmerProduct.setPrice(farmerProductDto.getPrice());
        farmerProduct.setDescription(farmerProductDto.getDescription());
        farmerProduct.setAvailableQuantity(farmerProductDto.getAvailableQuantity());
        farmerProduct.setMinimumQuantity(farmerProductDto.getMinimumQuantity());
        farmerProduct.setAddedDate(farmerProductDto.getAddedDate());
        farmerProduct.setProductCategory(FarmerProductDto.ProductCategory.valueOf(farmerProductDto.getProductCategory()));

        // Ensure the user is assigned
        farmerProduct.setUser(user);

        // Set the flags based on business rules
        farmerProduct.setActive(true);
        farmerProduct.setDeleted(false);
        farmerProduct.setQuantityLowered(false);

        FarmerProductAddResponse response = new FarmerProductAddResponse();
        try {
            // Save or update the product
            FarmerProduct savedProduct = farmerProductRepository.save(farmerProduct);

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
    public FarmerProductGetResponse GetAllFarmerProducts() {
        FarmerProductGetResponse response = new FarmerProductGetResponse();
        try {
            // Fetch all user details
            List<FarmerProduct> farmerProductList = farmerProductRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<FarmerProductDto> farmerProductDtoList = farmerProductList.stream()
                    .map(farmerProduct -> {
                        FarmerProductDto dto = new FarmerProductDto();
                        dto.setProductID(farmerProduct.getProductID());
                        dto.setProductName(farmerProduct.getProductName());
                        dto.setProductImage(farmerProduct.getProductImage());
                        dto.setPrice(farmerProduct.getPrice());
                        dto.setAvailableQuantity(farmerProduct.getAvailableQuantity());
                        dto.setMinimumQuantity(farmerProduct.getMinimumQuantity());
                        dto.setAddedDate(farmerProduct.getAddedDate());
                        dto.setDescription(farmerProduct.getDescription());
                        dto.setActive(farmerProduct.isActive());
                        dto.setDeleted(farmerProduct.isDeleted());
                        dto.setQuantityLowered(farmerProduct.isQuantityLowered());
                        dto.setProductCategory(String.valueOf(farmerProduct.getProductCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerProduct.getUser().getUserID());
                        userDto.setUserEmail(farmerProduct.getUser().getUserEmail());
                        userDto.setFirstName(farmerProduct.getUser().getFirstName());
                        userDto.setLastName(farmerProduct.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerProduct.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerProductGetResponse(farmerProductDtoList);
            response.setStatus("200");
            response.setMessage("Product Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving product Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

}
