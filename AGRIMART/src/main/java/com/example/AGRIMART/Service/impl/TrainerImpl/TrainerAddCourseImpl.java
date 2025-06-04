package com.example.AGRIMART.Service.impl.TrainerImpl;


import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderAddToCartResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderRemovedFromCartResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.*;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerAddCourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerAddCourseImpl implements TrainerAddCourseService {

    @Autowired
    private TrainerAddCourseRepository trainerAddCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerAddCourseAddResponse saveOrUpdate(TrainerAddCourseDto trainerAddCourseDto) {

        String username = (String) session.getAttribute("userEmail");

        if (username == null || username.isEmpty()) {
            TrainerAddCourseAddResponse response = new TrainerAddCourseAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            TrainerAddCourseAddResponse response = new TrainerAddCourseAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        Optional<TrainerCourse> existingProductOptional = trainerAddCourseRepository.findById(trainerAddCourseDto.getCourseID());

        TrainerCourse trainerCourse;
        String actionPerformed;

        if (!existingProductOptional.isEmpty()) {
            trainerCourse = existingProductOptional.get();
            actionPerformed = "updated";
        } else {
            trainerCourse = new TrainerCourse();
            trainerCourse.setUser(user);

            actionPerformed = "added";
        }
        // If product exists, we update the fields; if not, we create a new one
        trainerCourse.setCourseName(trainerAddCourseDto.getCourseName());
        trainerCourse.setPrice(trainerAddCourseDto.getPrice());
        trainerCourse.setDescription(trainerAddCourseDto.getDescription());
        trainerCourse.setDriveLink(trainerAddCourseDto.getDriveLink());
        trainerCourse.setYoutubeLink(trainerAddCourseDto.getYoutubeLink());
        trainerCourse.setAddedDate(trainerAddCourseDto.getAddedDate());
        trainerCourse.setCourseCategory(TrainerAddCourseDto.CourseCategory.valueOf(trainerAddCourseDto.getCourseCategory()));

        // Ensure the user is assigned
        trainerCourse.setUser(user);

        // Set the flags based on business rules
        trainerCourse.setDeleted(false);
        trainerCourse.setAddedToCart(false);
        trainerCourse.setRemovedFromCart(false);



        TrainerAddCourseAddResponse response = new TrainerAddCourseAddResponse();
        try {
            // Save or update the product
            TrainerCourse savedAddCourse = trainerAddCourseRepository.save(trainerCourse);

            if (savedAddCourse != null) {
                response.setMessage("Course Details was saved/updated successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
                response.setCourseID(savedAddCourse.getCourseID()); // Return auto-generated ID
                System.out.println("Saved Product ID: " + savedAddCourse.getCourseID());
            } else {
                response.setMessage("Failed to save/update Course Details.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public TrainerAddCourseGetResponse GetAllTrainerCourses() {
        TrainerAddCourseGetResponse response = new TrainerAddCourseGetResponse();
        try {
            // Fetch all user details
            List<TrainerCourse> trainerCourseList = trainerAddCourseRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<TrainerAddCourseDto> trainerAddCourseDtoList = trainerCourseList.stream()
                    .map(trainerCourse -> {
                        TrainerAddCourseDto dto = new TrainerAddCourseDto();
                        dto.setCourseID(trainerCourse.getCourseID());
                        dto.setCourseName(trainerCourse.getCourseName());
                        dto.setPrice(trainerCourse.getPrice());
                        dto.setDriveLink(trainerCourse.getDriveLink());
                        dto.setYoutubeLink(trainerCourse.getYoutubeLink());
                        dto.setAddedDate(trainerCourse.getAddedDate());
                        dto.setDescription(trainerCourse.getDescription());
                        dto.setDeleted(trainerCourse.isDeleted());
                        dto.setAddedToCart(trainerCourse.isAddedToCart());
                        dto.setRemovedFromCart(trainerCourse.isRemovedFromCart());
                        dto.setCourseCategory(String.valueOf(trainerCourse.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerCourse.getUser().getUserID());
                        userDto.setUserEmail(trainerCourse.getUser().getUserEmail());
                        userDto.setFirstName(trainerCourse.getUser().getFirstName());
                        userDto.setLastName(trainerCourse.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerCourse.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerAddCourseGetResponse(trainerAddCourseDtoList);
            response.setStatus("200");
            response.setMessage("Course Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Course Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }


    public TrainerAddCourseGetResponse getTrainerAddCourseByUserId(int userID) {
        TrainerAddCourseGetResponse response = new TrainerAddCourseGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<TrainerCourse> trainerCourseList = trainerAddCourseRepository.findByUser_UserID(userID);

            if (trainerCourseList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<TrainerAddCourseDto> trainerAddCourseDtoList = trainerCourseList.stream()
                    .map(trainerCourse -> {
                        TrainerAddCourseDto dto = new TrainerAddCourseDto();
                        dto.setCourseID(trainerCourse.getCourseID());
                        dto.setCourseName(trainerCourse.getCourseName());
                        dto.setPrice(trainerCourse.getPrice());
                        dto.setDriveLink(trainerCourse.getDriveLink());
                        dto.setYoutubeLink(trainerCourse.getYoutubeLink());
                        dto.setAddedDate(trainerCourse.getAddedDate());
                        dto.setDescription(trainerCourse.getDescription());
                        dto.setDeleted(trainerCourse.isDeleted());
                        dto.setAddedToCart(trainerCourse.isAddedToCart());
                        dto.setRemovedFromCart(trainerCourse.isRemovedFromCart());
                        dto.setCourseCategory(String.valueOf(trainerCourse.getCourseCategory()));



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(trainerCourse.getUser().getUserID());
                        userDto.setUserEmail(trainerCourse.getUser().getUserEmail());
                        userDto.setFirstName(trainerCourse.getUser().getFirstName());
                        userDto.setLastName(trainerCourse.getUser().getLastName());
                        userDto.setUserType(String.valueOf(trainerCourse.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setTrainerAddCourseGetResponse(trainerAddCourseDtoList);
            response.setStatus("200");
            response.setMessage("Course Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Course Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public TrainerAddCourseDeleteResponse DeleteTrainerResponse(int courseID) {
        TrainerAddCourseDeleteResponse response = new TrainerAddCourseDeleteResponse();

        Optional<TrainerCourse> productOptional = trainerAddCourseRepository.findByCourseID(courseID);

        if (productOptional.isEmpty()) {
            response.setMessage("Course not found for ID: " + courseID);
            response.setStatus("404");
            response.setResponseCode("11002");
            return response;
        }

        TrainerCourse trainerCourse = productOptional.get();



        try {
            trainerCourse.setDeleted(true);
            trainerAddCourseRepository.save(trainerCourse);
            response.setTrainerAddCourseDeleteResponse(trainerCourse);
            response.setMessage("Course Id : " + courseID + " Course delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

//    @Override
//    public TrainerCourseAddToCartResponse AddToCartTrainerCourseResponse(int courseID) {
//        TrainerCourseAddToCartResponse response = new TrainerCourseAddToCartResponse();
//
//        //calculation part
//        TrainerCourse trainerCourse;
//        trainerCourse = trainerAddCourseRepository.findByCourseID(courseID);
//
//
//
//        try {
//            trainerCourse.setAddedToCart(true);
//                trainerAddCourseRepository.save(trainerCourse);
//                response.setTrainerCourseAddToCartResponse(trainerCourse);
//                response.setMessage("course Id : " + courseID + " course add to cart successfully");
//                response.setStatus("200");
//                response.setResponseCode("11000");
//
//
//        }catch (Exception e){
//            response.setMessage("Error delete allocate item " + e.getMessage());
//            response.setResponseCode("11001");
//            response.setStatus("500");
//
//        }
//
//
//        return response;
//    }
//
//    @Override
//    public TrainerCourseRemovedFromCartResponse RemovedFromCartTrainerCourseResponse(int courseID) {
//        TrainerCourseRemovedFromCartResponse response = new TrainerCourseRemovedFromCartResponse();
//
//        TrainerCourse trainerCourse;
//        trainerCourse = trainerAddCourseRepository.findByCourseID(courseID);
//
//
//
//        try {
//            trainerCourse.setRemovedFromCart(true);
//            trainerAddCourseRepository.save(trainerCourse);
//            response.setTrainerCourseRemovedFromCartResponse(trainerCourse);
//            response.setMessage("course Id : " + courseID + " course add to cart successfully");
//            response.setStatus("200");
//            response.setResponseCode("11000");
//
//
//        }catch (Exception e){
//            response.setMessage("Error delete allocate item " + e.getMessage());
//            response.setResponseCode("11001");
//            response.setStatus("500");
//
//        }
//
//
//        return response;
//    }
}
