package com.example.AGRIMART.Service.impl.SFImpl;


import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SFResponse.SeedsAndFertilizerDeleteResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SFService.SFProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SFProductImpl implements SFProductService {

    @Autowired
    private SFProductRepository SFProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SFProductAddResponse saveOrUpdate(SFProductDto SFProductDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            SFProductAddResponse response = new SFProductAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            SFProductAddResponse response = new SFProductAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<SFProduct> existingProductOptional = SFProductRepository.findById(SFProductDto.getProductID());
        SFProduct SFProduct;
        String actionPerformed;

        if (!existingProductOptional.isEmpty()) {
            SFProduct = existingProductOptional.get();
            actionPerformed = "updated";
        } else {
            SFProduct = new SFProduct();
            SFProduct.setUser(user);

            actionPerformed = "added";
        }
        // If product exists, we update the fields; if not, we create a new one
        SFProduct.setProductName(SFProductDto.getProductName());
        SFProduct.setPrice(SFProductDto.getPrice());
        SFProduct.setDescription(SFProductDto.getDescription());
        SFProduct.setAvailableQuantity(SFProductDto.getAvailableQuantity());
        SFProduct.setMinimumQuantity(SFProductDto.getMinimumQuantity());
        SFProduct.setAddedDate(SFProductDto.getAddedDate());
        SFProduct.setProductCategory(com.example.AGRIMART.Dto.SFDto.SFProductDto.ProductCategory.valueOf(SFProductDto.getProductCategory()));

        // Ensure the user is assigned
        SFProduct.setUser(user);

        // Set the flags based on business rules
        SFProduct.setActive(true);
        SFProduct.setDeleted(false);
        SFProduct.setQuantityLowered(false);

        SFProductAddResponse response = new SFProductAddResponse();
        try {
            // Save or update the product
            SFProduct savedProduct = SFProductRepository.save(SFProduct);

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
    public SFProductGetResponse GetAllSeedsAndFertilizerProducts() {
        SFProductGetResponse response = new SFProductGetResponse();
        try {
            // Fetch all user details
            List<SFProduct> SFProductList = SFProductRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<SFProductDto> SFProductDtoList = SFProductList.stream()
                    .map(seedsAndFertilizerProduct -> {
                        SFProductDto dto = new SFProductDto();
                        dto.setProductID(seedsAndFertilizerProduct.getProductID());
                        dto.setProductName(seedsAndFertilizerProduct.getProductName());
                        dto.setPrice(seedsAndFertilizerProduct.getPrice());
                        dto.setAvailableQuantity(seedsAndFertilizerProduct.getAvailableQuantity());
                        dto.setMinimumQuantity(seedsAndFertilizerProduct.getMinimumQuantity());
                        dto.setDescription(seedsAndFertilizerProduct.getDescription());
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

            response.setSeedsAndFertilizerProductGetResponse(SFProductDtoList);
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

    public SFProductGetResponse getSeedsAndFertilizerProductByUserId(int userID) {
        SFProductGetResponse response = new SFProductGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SFProduct> SFProductList = SFProductRepository.findByUser_UserID(userID);

            if (SFProductList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SFProductDto> SFProductDtoList = SFProductList.stream()
                    .map(seedsAndFertilizerProduct -> {
                        SFProductDto dto = new SFProductDto();
                        dto.setProductID(seedsAndFertilizerProduct.getProductID());
                        dto.setProductName(seedsAndFertilizerProduct.getProductName());
                        dto.setPrice(seedsAndFertilizerProduct.getPrice());
                        dto.setAvailableQuantity(seedsAndFertilizerProduct.getAvailableQuantity());
                        dto.setMinimumQuantity(seedsAndFertilizerProduct.getMinimumQuantity());
                        dto.setDescription(seedsAndFertilizerProduct.getDescription());
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

            response.setSeedsAndFertilizerProductGetResponse(SFProductDtoList);
            response.setStatus("200");
            response.setMessage("Product details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving product details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public SeedsAndFertilizerDeleteResponse DeleteSeedsAndFertilizerResponse(int productID) {
        SeedsAndFertilizerDeleteResponse response = new SeedsAndFertilizerDeleteResponse();

        //calculation part
        SFProduct SFProduct;
        SFProduct = SFProductRepository.findByProductID(productID);



        try {
            SFProduct.setDeleted(true);
            SFProductRepository.save(SFProduct);
            response.setSFProductDeleteResponse(SFProduct);
            response.setMessage("product Id : " + productID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

}
