package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringOfferDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseOfferGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseOffer;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerOfferRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerCourseOfferRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerCourseOfferService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerCourseOfferImpl implements TrainerCourseOfferService {

    @Autowired
    private TrainerCourseOfferRepository trainerCourseOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerAddCourseRepository trainerAddCourseRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerCourseOfferAddResponse saveOrUpdate(TrainerCourseOfferDto trainerCourseOfferDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer courseID = (Integer) session.getAttribute("courseID");

        if (username == null || username.isEmpty()) {
            TrainerCourseOfferAddResponse response = new TrainerCourseOfferAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerCourse> productOptional = trainerAddCourseRepository.findByCourseID(courseID);

        if (userOptional.isEmpty()) {
            TrainerCourseOfferAddResponse response = new TrainerCourseOfferAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            TrainerCourseOfferAddResponse response = new TrainerCourseOfferAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        TrainerCourse trainerCourse = productOptional.get();

        // Check if there's an existing offer for this product
        List<TrainerCourseOffer> existingOffers = trainerCourseOfferRepository.findByTrainerCourse_CourseID(trainerCourse.getCourseID());

        TrainerCourseOffer trainerCourseOffer;
        String actionPerformed;

        if (!existingOffers.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            trainerCourseOffer = existingOffers.get(0);
            actionPerformed = "updated";
        } else {
            // Create new offer
            trainerCourseOffer = new TrainerCourseOffer();
            trainerCourseOffer.setUser(user);
            trainerCourseOffer.setTrainerCourse(trainerCourse);
            actionPerformed = "added";
        }

        // Update fields
        trainerCourseOffer.setOfferName(trainerCourseOfferDto.getOfferName());
        trainerCourseOffer.setOfferDescription(trainerCourseOfferDto.getOfferDescription());
        trainerCourseOffer.setNewPrice(trainerCourseOfferDto.getNewPrice());
        trainerCourseOffer.setActive(true);

        TrainerCourseOfferAddResponse response = new TrainerCourseOfferAddResponse();
        try {
            // Save or update the offer
            TrainerCourseOffer savedOffer = trainerCourseOfferRepository.save(trainerCourseOffer);

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

    public TrainerCourseOfferGetResponse getTrainerCourseOffersByCourseId(int courseID) {
        TrainerCourseOfferGetResponse response = new TrainerCourseOfferGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerCourseOffer> trainerCourseOfferList = trainerCourseOfferRepository.findByTrainerCourse_CourseID(courseID);

            if (trainerCourseOfferList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No offers found for product ID: " + courseID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerCourseOfferDto> trainerCourseOfferDtoList = trainerCourseOfferList.stream()
                    .map(trainerCourseOffer -> {
                        TrainerCourseOfferDto dto = new TrainerCourseOfferDto();
                        dto.setOfferID(trainerCourseOffer.getOfferID());
                        dto.setOfferName(trainerCourseOffer.getOfferName());
                        dto.setOfferDescription(trainerCourseOffer.getOfferDescription());
                        dto.setNewPrice(trainerCourseOffer.getNewPrice());
                        dto.setActive(trainerCourseOffer.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerCourseOffer.getUser().getUserID());
                        userDto.setUserEmail(trainerCourseOffer.getUser().getUserEmail());
                        userDto.setFirstName(trainerCourseOffer.getUser().getFirstName());
                        userDto.setLastName(trainerCourseOffer.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerCourseOffer.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerCourseOfferGetResponse(trainerCourseOfferDtoList);
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
    public TrainerCourseOfferDeleteResponse DeleteTrainerCourseOfferResponse(int offerID) {
        TrainerCourseOfferDeleteResponse response = new TrainerCourseOfferDeleteResponse();

        //calculation part
        TrainerCourseOffer trainerCourseOffer;
        trainerCourseOffer = trainerCourseOfferRepository.findByOfferID(offerID);



        try {
            trainerCourseOffer.setActive(false);
            trainerCourseOfferRepository.save(trainerCourseOffer);
            response.setTrainerCourseOfferDeleteResponse(trainerCourseOffer);
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
