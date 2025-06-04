package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseChaptersDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseChapters;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerOfferRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerCourseChaptersRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerCourseChaptersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerCourseChaptersImpl implements TrainerCourseChaptersService {

    @Autowired
    private TrainerCourseChaptersRepository trainerCourseChaptersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerAddCourseRepository trainerAddCourseRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerCourseChaptersAddResponse saveOrUpdate(TrainerCourseChaptersDto trainerCourseChaptersDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer courseID = (Integer) session.getAttribute("courseID");

        if (username == null || username.isEmpty()) {
            TrainerCourseChaptersAddResponse response = new TrainerCourseChaptersAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerCourse> courseOptional = trainerAddCourseRepository.findByCourseID(courseID);

        if (userOptional.isEmpty()) {
            TrainerCourseChaptersAddResponse response = new TrainerCourseChaptersAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (courseOptional.isEmpty()) {
            TrainerCourseChaptersAddResponse response = new TrainerCourseChaptersAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }

        User user = userOptional.get();
        TrainerCourse trainerCourse = courseOptional.get();

        // Check if there's an existing offer for this product
        Optional<TrainerCourseChapters> existingChapterOptional = trainerCourseChaptersRepository.findById(trainerCourseChaptersDto.getChapterID());

        TrainerCourseChapters trainerCourseChapters;
        String actionPerformed;

        if (!existingChapterOptional.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            trainerCourseChapters = existingChapterOptional.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            trainerCourseChapters = new TrainerCourseChapters();
            trainerCourseChapters.setUser(user);
            trainerCourseChapters.setTrainerCourse(trainerCourse);
            actionPerformed = "added";
        }

        // Update fields
        trainerCourseChapters.setChapterNo(trainerCourseChaptersDto.getChapterNo());
        trainerCourseChapters.setChapterName(trainerCourseChaptersDto.getChapterName());
        trainerCourseChapters.setChapterDescription(trainerCourseChaptersDto.getChapterDescription());
        trainerCourseChapters.setActive(true);

        TrainerCourseChaptersAddResponse response = new TrainerCourseChaptersAddResponse();
        try {
            // Save or update the offer
            TrainerCourseChapters savedChapters = trainerCourseChaptersRepository.save(trainerCourseChapters);

            if (savedChapters != null) {
                response.setMessage("Chapter was " + actionPerformed + " successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to " + actionPerformed + " Chapter.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public TrainerCourseChaptersGetResponse getTrainerCourseChaptersByCourseID(int courseID) {
        TrainerCourseChaptersGetResponse response = new TrainerCourseChaptersGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerCourseChapters> trainerCourseChaptersList = trainerCourseChaptersRepository.findByTrainerCourse_CourseID(courseID);

            if (trainerCourseChaptersList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No Chapters found for Course ID: " + courseID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerCourseChaptersDto> trainerCourseChaptersDtoList = trainerCourseChaptersList.stream()
                    .map(trainerCourseChapters -> {
                        TrainerCourseChaptersDto dto = new TrainerCourseChaptersDto();
                        dto.setChapterID(trainerCourseChapters.getChapterID());
                        dto.setChapterName(trainerCourseChapters.getChapterName());
                        dto.setChapterNo(trainerCourseChapters.getChapterNo());
                        dto.setChapterDescription(trainerCourseChapters.getChapterDescription());
                        dto.setActive(trainerCourseChapters.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerCourseChapters.getUser().getUserID());
                        userDto.setUserEmail(trainerCourseChapters.getUser().getUserEmail());
                        userDto.setFirstName(trainerCourseChapters.getUser().getFirstName());
                        userDto.setLastName(trainerCourseChapters.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerCourseChapters.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerCourseChaptersGetResponse(trainerCourseChaptersDtoList);
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
    public TrainerCourseChaptersDeleteResponse DeleteTrainerCourseChaptersResponse(int chapterID) {
        TrainerCourseChaptersDeleteResponse response = new TrainerCourseChaptersDeleteResponse();

        //calculation part
        TrainerCourseChapters trainerCourseChapters;
        trainerCourseChapters = trainerCourseChaptersRepository.findByChapterID(chapterID);



        try {
            trainerCourseChapters.setActive(false);
            trainerCourseChaptersRepository.save(trainerCourseChapters);
            response.setTrainerCourseChaptersDeleteResponse(trainerCourseChapters);
            response.setMessage("Chapter Id : " + chapterID + " Chapter deleted successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete Chapters " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }
}
