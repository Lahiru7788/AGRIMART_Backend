package com.example.AGRIMART.Service.impl.SFImpl;

import com.example.AGRIMART.Dto.SFDto.SFOfferDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SFResponse.SFOfferAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOfferGetResponse;
import com.example.AGRIMART.Entity.SFEntity.SFOffer;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.SFRepository.SFOfferRepository;
import com.example.AGRIMART.Repository.SFRepository.SFProductRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.SFService.SFOfferService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SFOfferImpl implements SFOfferService {

    @Autowired
    private SFOfferRepository sfOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SFProductRepository SFProductRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SFOfferAddResponse saveOrUpdate(SFOfferDto sfOfferDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer productID = (Integer) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            SFOfferAddResponse response = new SFOfferAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SFProduct> productOptional = SFProductRepository.findByProductID(productID);


        if (userOptional.isEmpty()) {
            SFOfferAddResponse response = new SFOfferAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            SFOfferAddResponse response = new SFOfferAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        SFProduct SFProduct = productOptional.get();

        // Check if there's an existing offer for this product
        List<SFOffer> existingOffers = sfOfferRepository.findBySFProduct_ProductID(SFProduct.getProductID());

        SFOffer sfOffer;
        String actionPerformed;

        if (!existingOffers.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            sfOffer = existingOffers.get(0);
            actionPerformed = "updated";
        } else {
            // Create new offer
            sfOffer = new SFOffer();
            sfOffer.setUser(user);
            sfOffer.setSFProduct(SFProduct);
            actionPerformed = "added";
        }

        // Update fields
        sfOffer.setOfferName(sfOfferDto.getOfferName());
        sfOffer.setOfferDescription(sfOfferDto.getOfferDescription());
        sfOffer.setNewPrice(sfOfferDto.getNewPrice());
        sfOffer.setActive(true);

        SFOfferAddResponse response = new SFOfferAddResponse();
        try {
            // Save or update the offer
            SFOffer savedOffer = sfOfferRepository.save(sfOffer);

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

    public SFOfferGetResponse getSFOffersByProductId(int productID) {
        SFOfferGetResponse response = new SFOfferGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SFOffer> farmerOfferList = sfOfferRepository.findBySFProduct_ProductID(productID);

            if (farmerOfferList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No offers found for product ID: " + productID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SFOfferDto> farmerOfferDtoList = farmerOfferList.stream()
                    .map(sfOffer -> {
                        SFOfferDto dto = new SFOfferDto();
                        dto.setOfferID(sfOffer.getOfferID());
                        dto.setOfferName(sfOffer.getOfferName());
                        dto.setOfferDescription(sfOffer.getOfferDescription());
                        dto.setNewPrice(sfOffer.getNewPrice());
                        dto.setActive(sfOffer.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(sfOffer.getUser().getUserID());
                        userDto.setUserEmail(sfOffer.getUser().getUserEmail());
                        userDto.setFirstName(sfOffer.getUser().getFirstName());
                        userDto.setLastName(sfOffer.getUser().getLastName());
                        userDto.setUserType(String.valueOf(sfOffer.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSfOfferGetResponse(farmerOfferDtoList);
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
    public SFOfferDeleteResponse DeleteSFResponse(int offerID) {
        SFOfferDeleteResponse response = new SFOfferDeleteResponse();

        //calculation part
        SFOffer sfOffer;
        sfOffer = sfOfferRepository.findByOfferID(offerID);



        try {
            sfOffer.setActive(false);
            sfOfferRepository.save(sfOffer);
            response.setSfOfferDeleteResponse(sfOffer);
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
