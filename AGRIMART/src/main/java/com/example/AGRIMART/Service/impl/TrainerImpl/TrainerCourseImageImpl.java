package com.example.AGRIMART.Service.impl.TrainerImpl;

import com.example.AGRIMART.Dto.SupermarketDto.SAddOrderImageDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseImageDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseImageAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseImageGetResponse;
import com.example.AGRIMART.Entity.SupermarketEntity.SAddOrderImage;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.SupermarketRepository.SAddOrderImageRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketAddOrderRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerAddCourseRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerCourseImageRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.TrainerService.TrainerCourseImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerCourseImageImpl implements TrainerCourseImageService {

    @Autowired
    private TrainerCourseImageRepository trainerCourseImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerAddCourseRepository trainerAddCourseRepository;

    @Autowired
    private HttpSession session;

    @Override
    public TrainerCourseImageAddResponse save(TrainerCourseImageDto trainerCourseImageDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        Integer courseID = (Integer) session.getAttribute("courseID");

        if (username == null || username.isEmpty()) {
            TrainerCourseImageAddResponse response = new TrainerCourseImageAddResponse();
            response.setMessage("User is not logged in , Order is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

//        if ( null == productID || productID.isEmpty()) {
//            FProductImageAddResponse response = new FProductImageAddResponse();
//            response.setMessage("User is not logged in , Product is not available in the store or session expired.");
//            response.setStatus("401"); // Unauthorized
//            return response;
//        }
        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerCourse> courseOptional = trainerAddCourseRepository.findByCourseID(courseID);


        if (userOptional.isEmpty()) {
            TrainerCourseImageAddResponse response = new TrainerCourseImageAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (courseOptional.isEmpty()) {
            TrainerCourseImageAddResponse response = new TrainerCourseImageAddResponse();
            response.setMessage("Order not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        TrainerCourse trainerCourse = courseOptional.get();

        Optional<TrainerCourseImage> existingImageOptional = trainerCourseImageRepository.findByTrainerCourse_CourseID(trainerCourse.getCourseID());

        TrainerCourseImage trainerCourseImage;
        String actionPerformed;

        if (existingImageOptional.isPresent()) {
            // Update existing image
            trainerCourseImage = existingImageOptional.get();
            trainerCourseImage.setCourseImage(trainerCourseImageDto.getCourseImage());
            actionPerformed = "updated";
        } else {
            trainerCourseImage = new TrainerCourseImage();
            trainerCourseImage.setCourseImage(trainerCourseImageDto.getCourseImage());
            trainerCourseImage.setUser(user);
            trainerCourseImage.setTrainerCourse(trainerCourse);
            actionPerformed = "added";
        }
        TrainerCourseImageAddResponse response = new TrainerCourseImageAddResponse();
        try {
            TrainerCourseImage saveCourseImage = trainerCourseImageRepository.save(trainerCourseImage);
            if (saveCourseImage != null) {
                response.setMessage("Order Image was added successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to add Order Image.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

//    public CAddOrderImageGetResponse GetAllConsumerAddOrderImages() {
//        CAddOrderImageGetResponse response = new CAddOrderImageGetResponse();
//        try {
//            // Fetch all user details
//            List<CAddOrderImage> cAddOrderImageList = cAddOrderImageRepository.findAll();
//
//            // Map UserDetails entities to a simplified DTO without sensitive data
//            List<CAddOrderImageDto> cAddOrderImageDtoList = cAddOrderImageList.stream()
//                    .map(cAddOrderImage -> {
//                        CAddOrderImageDto dto = new CAddOrderImageDto();
//                        dto.setImageID(cAddOrderImage.getImageID());
//                        dto.setProductImage(cAddOrderImage.getProductImage());
//
//
//                        // Map nested user information without credentials
//                        UserDto userDto = new UserDto();
//                        userDto.setUserID(cAddOrderImage.getUser().getUserID());
//                        userDto.setUserEmail(cAddOrderImage.getUser().getUserEmail());
//                        userDto.setFirstName(cAddOrderImage.getUser().getFirstName());
//                        userDto.setLastName(cAddOrderImage.getUser().getLastName());
//                        userDto.setUserType(String.valueOf(cAddOrderImage.getUser().getUserType()));
//
//
//                        dto.setUser(userDto);
//                        return dto;
//                    })
//                    .collect(Collectors.toList());
//
//            response.setCAddOrderImageGetResponse(cAddOrderImageDtoList);
//            response.setStatus("200");
//            response.setMessage("Order images retrieved successfully");
//            response.setResponseCode("1600");
//
//        } catch (Exception e) {
//            response.setStatus("500");
//            response.setMessage("Error retrieving Order Images: " + e.getMessage());
//            response.setResponseCode("1601");
//        }
//
//        return response;
//    }

    @Override
    public TrainerCourseImageGetResponse GetTrainerCourseImageFindById(int courseID) {
        TrainerCourseImageGetResponse response = new TrainerCourseImageGetResponse();
        try {
            Optional<TrainerCourseImage> trainerCourseImageOptional = trainerCourseImageRepository.findById(courseID);

            if (trainerCourseImageOptional.isPresent()) {
                TrainerCourseImage trainerCourseImage = trainerCourseImageOptional.get();
                TrainerCourseImageDto dto = new TrainerCourseImageDto();
                dto.setImageID(trainerCourseImage.getImageID());
                dto.setCourseImage(trainerCourseImage.getCourseImage());

                UserDto userDto = new UserDto();
                userDto.setUserID(trainerCourseImage.getUser().getUserID());
                userDto.setUserEmail(trainerCourseImage.getUser().getUserEmail());
                userDto.setFirstName(trainerCourseImage.getUser().getFirstName());
                userDto.setLastName(trainerCourseImage.getUser().getLastName());
                userDto.setUserType(String.valueOf(trainerCourseImage.getUser().getUserType()));

                dto.setUser(userDto);

                response.setTrainerCourseImageGetResponse(List.of(dto));
                response.setStatus("200");
                response.setMessage("Profile image retrieved successfully.");
                response.setResponseCode("1602");
            } else {
                response.setStatus("404");
                response.setMessage("Profile image not found.");
                response.setResponseCode("1603");
            }
        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving Profile image: " + e.getMessage());
            response.setResponseCode("1604");
        }
        return response;
    }
}
