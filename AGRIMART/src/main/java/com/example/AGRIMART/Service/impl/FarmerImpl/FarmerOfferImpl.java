package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerOfferRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerOfferService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerOfferImpl implements FarmerOfferService {

    @Autowired
    private FarmerOfferRepository farmerOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProductRepository farmerProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerOfferAddResponse saveOrUpdate(FarmerOfferDto farmerOfferDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FarmerOfferAddResponse response = new FarmerOfferAddResponse();
            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductName(productID);

        if (userOptional.isEmpty()) {
            FarmerOfferAddResponse response = new FarmerOfferAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (productOptional.isEmpty()) {
            FarmerOfferAddResponse response = new FarmerOfferAddResponse();
            response.setMessage(" Product not found for the given product name.");
            return response;
        }



        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();
        Optional<FarmerOffer> existingProductOptional = farmerOfferRepository.findById(farmerOfferDto.getOfferID());

        FarmerOffer farmerOffer = existingProductOptional.orElse(new FarmerOffer());
        // If product exists, we update the fields; if not, we create a new one
        farmerOffer.setOfferName(farmerOfferDto.getOfferName());
        farmerOffer.setOfferDescription(farmerOfferDto.getOfferDescription());
        farmerOffer.setNewPrice(farmerOfferDto.getNewPrice());

        // Ensure the user is assigned
        farmerOffer.setUser(user);
        farmerOffer.setFarmerProduct(farmerProduct);

        // Set the flags based on business rules
        farmerOffer.setActive(true);


        FarmerOfferAddResponse response = new FarmerOfferAddResponse();
        try {
            // Save or update the product
            FarmerOffer savedOffer = farmerOfferRepository.save(farmerOffer);

            if (savedOffer != null) {
                response.setMessage("Offer was saved/updated successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to save/update offer.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public FarmerOfferGetResponse GetAllFarmerOffers() {
        FarmerOfferGetResponse response = new FarmerOfferGetResponse();
        try {
            // Fetch all user details
            List<FarmerOffer> farmerOfferList = farmerOfferRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<FarmerOfferDto> farmerOfferDtoList = farmerOfferList.stream()
                    .map(farmerOffer -> {
                        FarmerOfferDto dto = new FarmerOfferDto();
                        dto.setOfferID(farmerOffer.getOfferID());
                        dto.setOfferName(farmerOffer.getOfferName());
                        dto.setOfferDescription(farmerOffer.getOfferDescription());
                        dto.setNewPrice(farmerOffer.getNewPrice());
                        dto.setActive(farmerOffer.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerOffer.getUser().getUserID());
                        userDto.setUserEmail(farmerOffer.getUser().getUserEmail());
                        userDto.setFirstName(farmerOffer.getUser().getFirstName());
                        userDto.setLastName(farmerOffer.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerOffer.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerOfferGetResponse(farmerOfferDtoList);
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
