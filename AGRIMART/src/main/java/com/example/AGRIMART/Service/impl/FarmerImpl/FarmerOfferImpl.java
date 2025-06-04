package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
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
        Integer productID = (Integer) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FarmerOfferAddResponse response = new FarmerOfferAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<FarmerProduct> productOptional = farmerProductRepository.findByProductID(productID);

        if (userOptional.isEmpty()) {
            FarmerOfferAddResponse response = new FarmerOfferAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            FarmerOfferAddResponse response = new FarmerOfferAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        FarmerProduct farmerProduct = productOptional.get();

        // Check if there's an existing offer for this product
        List<FarmerOffer> existingOffers = farmerOfferRepository.findByFarmerProduct_ProductID(farmerProduct.getProductID());

        FarmerOffer farmerOffer;
        String actionPerformed;

        if (!existingOffers.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            farmerOffer = existingOffers.get(0);
            actionPerformed = "updated";
        } else {
            // Create new offer
            farmerOffer = new FarmerOffer();
            farmerOffer.setUser(user);
            farmerOffer.setFarmerProduct(farmerProduct);
            actionPerformed = "added";
        }

        // Update fields
        farmerOffer.setOfferName(farmerOfferDto.getOfferName());
        farmerOffer.setOfferDescription(farmerOfferDto.getOfferDescription());
        farmerOffer.setNewPrice(farmerOfferDto.getNewPrice());
        farmerOffer.setActive(true);

        FarmerOfferAddResponse response = new FarmerOfferAddResponse();
        try {
            // Save or update the offer
            FarmerOffer savedOffer = farmerOfferRepository.save(farmerOffer);

            if (savedOffer != null) {
                response.setMessage("Offer was " + actionPerformed + " successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to " + actionPerformed + " offer.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public FarmerOfferGetResponse getFarmerOffersByProductId(int productID) {
        FarmerOfferGetResponse response = new FarmerOfferGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerOffer> farmerOfferList = farmerOfferRepository.findByFarmerProduct_ProductID(productID);

            if (farmerOfferList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No offers found for product ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
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
            response.setMessage("Product offers retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving product offers: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public FarmerOfferDeleteResponse DeleteFarmerResponse(int offerID) {
        FarmerOfferDeleteResponse response = new FarmerOfferDeleteResponse();

        //calculation part
        FarmerOffer farmerOffer;
        farmerOffer = farmerOfferRepository.findByOfferID(offerID);



        try {
            farmerOffer.setActive(false);
            farmerOfferRepository.save(farmerOffer);
            response.setFarmerOfferDeleteResponse(farmerOffer);
            response.setMessage("Offer Id : " + offerID + " item delete successfully");
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
