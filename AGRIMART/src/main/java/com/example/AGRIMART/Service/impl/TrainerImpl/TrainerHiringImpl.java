package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerHiringRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerHiringService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerHiringImpl implements TrainerHiringService {

    @Autowired
    private TrainerHiringRepository trainerHiringRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerHiringAddResponse saveOrUpdate(TrainerHiringDto trainerHiringDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            TrainerHiringAddResponse response = new TrainerHiringAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            TrainerHiringAddResponse response = new TrainerHiringAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<TrainerHiring> existingProductOptional = trainerHiringRepository.findById(trainerHiringDto.getHireID());

        TrainerHiring trainerHiring;
        String actionPerformed;

        if (!existingProductOptional.isEmpty()) {
            trainerHiring = existingProductOptional.get();
            actionPerformed = "updated";
        } else {
            trainerHiring = new TrainerHiring();
            trainerHiring.setUser(user);

            actionPerformed = "added";
        }
        // If product exists, we update the fields; if not, we create a new one
        trainerHiring.setName(trainerHiringDto.getName());
        trainerHiring.setPrice(trainerHiringDto.getPrice());
        trainerHiring.setQualifications(trainerHiringDto.getQualifications());
        trainerHiring.setYearsOfExperience(trainerHiringDto.getYearsOfExperience());
        trainerHiring.setAddedDate(trainerHiringDto.getAddedDate());
        // Ensure the user is assigned
        trainerHiring.setUser(user);

        // Set the flags based on business rules
        trainerHiring.setDeleted(false);




        TrainerHiringAddResponse response = new TrainerHiringAddResponse();
        try {
            // Save or update the product
            TrainerHiring savedAddHiring = trainerHiringRepository.save(trainerHiring);

            if (savedAddHiring != null) {
                response.setMessage("Hiring Details was saved/updated successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
                response.setProductID(savedAddHiring.getHireID()); // Return auto-generated ID
                System.out.println("Saved Hire ID: " + savedAddHiring.getHireID());
            } else {
                response.setMessage("Failed to save/update Hiring Details.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public TrainerHiringGetResponse GetAllTrainerHirings() {
        TrainerHiringGetResponse response = new TrainerHiringGetResponse();
        try {
            // Fetch all user details
            List<TrainerHiring> trainerHiringList = trainerHiringRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<TrainerHiringDto> trainerHiringDtoList = trainerHiringList.stream()
                    .map(trainerHiring -> {
                        TrainerHiringDto dto = new TrainerHiringDto();
                        dto.setHireID(trainerHiring.getHireID());
                        dto.setName(trainerHiring.getName());
                        dto.setPrice(trainerHiring.getPrice());
                        dto.setQualifications(trainerHiring.getQualifications());
                        dto.setYearsOfExperience(trainerHiring.getYearsOfExperience());
                        dto.setAddedDate(trainerHiring.getAddedDate());
                        dto.setDeleted(trainerHiring.isDeleted());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerHiring.getUser().getUserID());
                        userDto.setUserEmail(trainerHiring.getUser().getUserEmail());
                        userDto.setFirstName(trainerHiring.getUser().getFirstName());
                        userDto.setLastName(trainerHiring.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerHiring.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerHiringGetResponse(trainerHiringDtoList);
            response.setStatus("200");
            response.setMessage("Hiring Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Hiring Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }


    public TrainerHiringGetResponse getTrainerHiringByUserID(int userID) {
        TrainerHiringGetResponse response = new TrainerHiringGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerHiring> trainerHiringList = trainerHiringRepository.findByUser_UserID(userID);

            if (trainerHiringList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerHiringDto> trainerHiringDtoList = trainerHiringList.stream()
                    .map(trainerHiring -> {
                        TrainerHiringDto dto = new TrainerHiringDto();
                        dto.setHireID(trainerHiring.getHireID());
                        dto.setName(trainerHiring.getName());
                        dto.setPrice(trainerHiring.getPrice());
                        dto.setQualifications(trainerHiring.getQualifications());
                        dto.setYearsOfExperience(trainerHiring.getYearsOfExperience());
                        dto.setAddedDate(trainerHiring.getAddedDate());
                        dto.setDeleted(trainerHiring.isDeleted());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerHiring.getUser().getUserID());
                        userDto.setUserEmail(trainerHiring.getUser().getUserEmail());
                        userDto.setFirstName(trainerHiring.getUser().getFirstName());
                        userDto.setLastName(trainerHiring.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerHiring.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerHiringGetResponse(trainerHiringDtoList);
            response.setStatus("200");
            response.setMessage("Hiring Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Hiring Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public TrainerHiringDeleteResponse DeleteTrainerHiringResponse(int hireID) {
        TrainerHiringDeleteResponse response = new TrainerHiringDeleteResponse();

        Optional<TrainerHiring> productOptional = trainerHiringRepository.findByHireID(hireID);

        if (productOptional.isEmpty()) {
            response.setMessage("Hire not found for ID: " + hireID);
            response.setStatus("404");
            response.setResponseCode("11002");
            return response;
        }

        TrainerHiring trainerHiring = productOptional.get();


        try {
            trainerHiring.setDeleted(true);
            trainerHiringRepository.save(trainerHiring);
            response.setTrainerHiringDeleteResponse(trainerHiring);
            response.setMessage("Hire Id : " + hireID + " Hire delete successfully");
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
