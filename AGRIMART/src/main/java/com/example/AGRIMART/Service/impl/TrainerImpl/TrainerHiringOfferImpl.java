package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringOfferDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerHiringOfferAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerHiringOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerHiringOfferGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiringOffer;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerOfferRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerHiringOfferRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerHiringRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerHiringOfferService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerHiringOfferImpl implements TrainerHiringOfferService {

    @Autowired
    private TrainerHiringOfferRepository trainerHiringOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerHiringRepository trainerHiringRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerHiringOfferAddResponse saveOrUpdate( TrainerHiringOfferDto trainerHiringOfferDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer hireID = (Integer) session.getAttribute("hireID");

        if (username == null || username.isEmpty()) {
            TrainerHiringOfferAddResponse response = new TrainerHiringOfferAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerHiring> productOptional = trainerHiringRepository.findByHireID(hireID);

        if (userOptional.isEmpty()) {
            TrainerHiringOfferAddResponse response = new TrainerHiringOfferAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            TrainerHiringOfferAddResponse response = new TrainerHiringOfferAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        TrainerHiring trainerHiring = productOptional.get();

        // Check if there's an existing offer for this product
        List<TrainerHiringOffer> existingOffers = trainerHiringOfferRepository.findByTrainerHiring_HireID(trainerHiring.getHireID());

        TrainerHiringOffer trainerHiringOffer;
        String actionPerformed;

        if (!existingOffers.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            trainerHiringOffer = existingOffers.get(0);
            actionPerformed = "updated";
        } else {
            // Create new offer
            trainerHiringOffer = new TrainerHiringOffer();
            trainerHiringOffer.setUser(user);
            trainerHiringOffer.setTrainerHiring(trainerHiring);
            actionPerformed = "added";
        }

        // Update fields
        trainerHiringOffer.setOfferName(trainerHiringOfferDto.getOfferName());
        trainerHiringOffer.setOfferDescription(trainerHiringOfferDto.getOfferDescription());
        trainerHiringOffer.setNewPrice(trainerHiringOfferDto.getNewPrice());
        trainerHiringOffer.setActive(true);

        TrainerHiringOfferAddResponse response = new TrainerHiringOfferAddResponse();
        try {
            // Save or update the offer
            TrainerHiringOffer savedOffer = trainerHiringOfferRepository.save(trainerHiringOffer);

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

    public TrainerHiringOfferGetResponse getTrainerHiringOffersByHireId(int hireID) {
        TrainerHiringOfferGetResponse response = new TrainerHiringOfferGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerHiringOffer> trainerHiringOfferList = trainerHiringOfferRepository.findByTrainerHiring_HireID(hireID);

            if (trainerHiringOfferList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No offers found for product ID: " + hireID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerHiringOfferDto> trainerHiringOfferDtoList = trainerHiringOfferList.stream()
                    .map(trainerHiringOffer -> {
                        TrainerHiringOfferDto dto = new TrainerHiringOfferDto();
                        dto.setOfferID(trainerHiringOffer.getOfferID());
                        dto.setOfferName(trainerHiringOffer.getOfferName());
                        dto.setOfferDescription(trainerHiringOffer.getOfferDescription());
                        dto.setNewPrice(trainerHiringOffer.getNewPrice());
                        dto.setActive(trainerHiringOffer.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerHiringOffer.getUser().getUserID());
                        userDto.setUserEmail(trainerHiringOffer.getUser().getUserEmail());
                        userDto.setFirstName(trainerHiringOffer.getUser().getFirstName());
                        userDto.setLastName(trainerHiringOffer.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerHiringOffer.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerHiringOfferGetResponse(trainerHiringOfferDtoList);
            response.setStatus("200");
            response.setMessage("Hirie offers retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Hire offers: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public TrainerHiringOfferDeleteResponse DeleteTrainerHiringOfferResponse(int offerID) {
        TrainerHiringOfferDeleteResponse response = new TrainerHiringOfferDeleteResponse();

        //calculation part
        TrainerHiringOffer trainerHiringOffer;
        trainerHiringOffer = trainerHiringOfferRepository.findByOfferID(offerID);



        try {
            trainerHiringOffer.setActive(false);
            trainerHiringOfferRepository.save(trainerHiringOffer);
            response.setTrainerHiringOfferDeleteResponse(trainerHiringOffer);
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
