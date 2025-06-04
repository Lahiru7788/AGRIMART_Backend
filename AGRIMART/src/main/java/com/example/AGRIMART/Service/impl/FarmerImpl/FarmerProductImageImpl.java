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

//    @Override
//    public FProductImageAddResponse save(FarmerProductImageDto farmerProductImageDto) {
//        String username = (String) session.getAttribute("userEmail");
//        String productID = (String) session.getAttribute("productID");
//
//        if (username == null || username.isEmpty()) {
//            return new FProductImageAddResponse("User is not logged in or session expired.", "401", null);
//        }
//
//        Optional<User> userOptional = userRepository.findByUserEmail(username);
//        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductName(productID);
//
//        if (userOptional.isEmpty()) {
//            return new FProductImageAddResponse("User not found for the given username.", "404", null);
//        }
//
//        if (productOptional.isEmpty()) {
//            return new FProductImageAddResponse("Product not found for the given product name.", "404", null);
//        }
//
//        User user = userOptional.get();
//        FarmerProduct farmerProduct = productOptional.get();
//        Optional<FarmerProductImage> existingProductOptional = fProductImageRepository.findById(farmerProductImageDto.getImageID());
//
//        FarmerProductImage farmerProductImage = existingProductOptional.orElse(new FarmerProductImage());
//        farmerProductImage.setProductImage(farmerProductImageDto.getProductImage());
//        farmerProductImage.setUser(user);
//        farmerProductImage.setFarmerProduct(farmerProduct);
//
//        try {
//            FarmerProductImage savedProductImage = fProductImageRepository.save(farmerProductImage);
//            if (savedProductImage != null) {
//                return new FProductImageAddResponse("Product Image added successfully.", "200", "1000");
//            } else {
//                return new FProductImageAddResponse("Failed to add Product Image.", "400", null);
//            }
//        } catch (Exception e) {
//            return new FProductImageAddResponse("Error: " + e.getMessage(), "500", null);
//        }
//    }

//    @Override
//    public FProductImageAddResponse save(FarmerProductImageDto farmerProductImageDto) {
//        // Retrieve username from session
//        String username = (String) session.getAttribute("userEmail");
//        String productID = (String) session.getAttribute("productID");
//
//        if (username == null || username.isEmpty()) {
//            FProductImageAddResponse response = new FProductImageAddResponse();
//            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
//            response.setStatus("401"); // Unauthorized
//            return response;
//        }
//
////        if ( null == productID || productID.isEmpty()) {
////            FProductImageAddResponse response = new FProductImageAddResponse();
////            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
////            response.setStatus("401"); // Unauthorized
////            return response;
////        }
//        // Find user by username
//        Optional<User> userOptional = userRepository.findByUserEmail(username);
//        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductName(productID);
//
//        if (userOptional.isEmpty()) {
//            FProductImageAddResponse response = new FProductImageAddResponse();
//            response.setMessage("User not found for the given username.");
//            return response;
//        }
//
//        if (productOptional.isEmpty()) {
//            FProductImageAddResponse response = new FProductImageAddResponse();
//            response.setMessage(" Product not found for the given product name.");
//            return response;
//        }
//
//        User user = userOptional.get();
//        FarmerProduct farmerProduct = productOptional.get();
//        Optional<FarmerProductImage> existingProductOptional = fProductImageRepository.findById(farmerProductImageDto.getProductID());
//
//        FarmerProductImage farmerProductImage = existingProductOptional.orElse(new FarmerProductImage());
//
//
//        farmerProductImage.setProductImage(farmerProductImageDto.getProductImage());
//
//        farmerProductImage.setUser(user);
//        farmerProductImage.setFarmerProduct(farmerProduct);
//
//        FProductImageAddResponse response = new FProductImageAddResponse();
//        try {
//            FarmerProductImage saveProductImage = fProductImageRepository.save(farmerProductImage);
//            if (saveProductImage != null) {
//                response.setMessage("Product Image was added successfully.");
//                response.setStatus("200");
//                response.setResponseCode("1000");
//            } else {
//                response.setMessage("Failed to add Product Image.");
//                response.setStatus("400");
//            }
//        } catch (Exception e) {
//            response.setMessage("Error: " + e.getMessage());
//            response.setStatus("500"); // Internal server error
//        }
//
//        return response;
//    }


    @Override
    public FProductImageAddResponse save(FarmerProductImageDto farmerProductImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer productID = (Integer) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FProductImageAddResponse response = new FProductImageAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductID(productID);

        if (userOptional.isEmpty()) {
            FProductImageAddResponse response = new FProductImageAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            FProductImageAddResponse response = new FProductImageAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();

        // Check if there's an existing image entry for this product
        Optional<FarmerProductImage> existingImageOptional = fProductImageRepository.findByFarmerProduct_ProductID(farmerProduct.getProductID());

        FarmerProductImage farmerProductImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            farmerProductImage = existingImageOptional.get();
            farmerProductImage.setProductImage(farmerProductImageDto.getProductImage());
            actionPerformed = "updated";
        } else {
            // Create new image entry
            farmerProductImage = new FarmerProductImage();
            farmerProductImage.setProductImage(farmerProductImageDto.getProductImage());
            farmerProductImage.setUser(user);
            farmerProductImage.setFarmerProduct(farmerProduct);
            actionPerformed = "added";
        }

        FProductImageAddResponse response = new FProductImageAddResponse();
        try {
            FarmerProductImage savedProductImage = fProductImageRepository.save(farmerProductImage);
            if (savedProductImage != null) {
                response.setMessage("Product Image was " + actionPerformed + " successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to " + actionPerformed + " Product Image.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

//    @Override
//    public FProductImageGetResponse GetAllFarmerProductImages() {
//        FProductImageGetResponse response = new FProductImageGetResponse();
//        try {
//            List<FarmerProductImage> farmerProductImageList = fProductImageRepository.findAll();
//
//            List<FarmerProductImageDto> farmerProductImageDtoList = farmerProductImageList.stream().map(farmerProductImage -> {
//                FarmerProductImageDto dto = new FarmerProductImageDto();
//                dto.setImageID(farmerProductImage.getImageID());
//                dto.setProductImage(farmerProductImage.getProductImage());
//
//                UserDto userDto = new UserDto();
//                userDto.setUserID(farmerProductImage.getUser().getUserID());
//                userDto.setUserEmail(farmerProductImage.getUser().getUserEmail());
//                userDto.setFirstName(farmerProductImage.getUser().getFirstName());
//                userDto.setLastName(farmerProductImage.getUser().getLastName());
//                userDto.setUserType(String.valueOf(farmerProductImage.getUser().getUserType()));
//
//                dto.setUser(userDto);
//                return dto;
//            }).collect(Collectors.toList());
//
//            response.setFProductImageGetResponse(farmerProductImageDtoList);
//            response.setStatus("200");
//            response.setMessage("Product images retrieved successfully.");
//            response.setResponseCode("1600");
//
//        } catch (Exception e) {
//            response.setStatus("500");
//            response.setMessage("Error retrieving product images: " + e.getMessage());
//            response.setResponseCode("1601");
//        }
//        return response;
//    }

    /**
     * ✅ Newly Implemented Method: Get Product Image by Product ID
     */
    @Override
    public FProductImageGetResponse GetFarmerProductImageFindById(int productID) {
        FProductImageGetResponse response = new FProductImageGetResponse();
        try {
            Optional<FarmerProductImage> productImageOptional = fProductImageRepository.findById(productID);

            if (productImageOptional.isPresent()) {
                FarmerProductImage productImage = productImageOptional.get();
                FarmerProductImageDto dto = new FarmerProductImageDto();
                dto.setImageID(productImage.getImageID());
                dto.setProductImage(productImage.getProductImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(productImage.getUser().getUserID());
                userDto.setUserEmail(productImage.getUser().getUserEmail());
                userDto.setFirstName(productImage.getUser().getFirstName());
                userDto.setLastName(productImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(productImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setFProductImageGetResponse(List.of(dto));
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
