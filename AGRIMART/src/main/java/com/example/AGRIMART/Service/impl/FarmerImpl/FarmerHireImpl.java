package com.example.AGRIMART.Service.impl.FarmerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerHireDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerHireDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.*;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerHireAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerHireDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerHirePaymentResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerHire;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerHire;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerHireRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerHireRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerProductRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerHiringRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerHireService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerHireImpl implements FarmerHireService {

    @Autowired
    private FarmerHireRepository farmerHireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerHiringRepository trainerHiringRepository;

    @Autowired
    private HttpSession session;

    @Override
    public FarmerHireAddResponse saveOrUpdate(FarmerHireDto farmerHireDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            FarmerHireAddResponse response = new FarmerHireAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerHiring> productOptional = trainerHiringRepository.findByHireID(farmerHireDto.getHireID());


        if (userOptional.isEmpty()) {
            FarmerHireAddResponse response = new FarmerHireAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            FarmerHireAddResponse response = new FarmerHireAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        TrainerHiring trainerHiring = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<FarmerHire> existingOrders = farmerHireRepository.findById(farmerHireDto.getOrderID());

        FarmerHire farmerHire;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            farmerHire = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            farmerHire = new FarmerHire();
            farmerHire.setUser(user);
            farmerHire.setTrainerHiring(trainerHiring);
            actionPerformed = "added";
        }

        // Update fields
        farmerHire.setName(farmerHireDto.getName());
        farmerHire.setPrice(farmerHireDto.getPrice());
        farmerHire.setQualifications(farmerHireDto.getQualifications());
        farmerHire.setYearsOfExperience(farmerHireDto.getYearsOfExperience());
        farmerHire.setAddedDate(farmerHireDto.getAddedDate());
        farmerHire.setActive(true);
        farmerHire.setUser(user);
        farmerHire.setTrainerHiring(trainerHiring);
        farmerHire.setConfirmed(false);
        farmerHire.setRejected(false);
        farmerHire.setAddedToCart(false);
        farmerHire.setRemovedFromCart(false);
        farmerHire.setPaid(false);

        FarmerHireAddResponse response = new FarmerHireAddResponse();
        try {
            // Save or update the offer
            FarmerHire savedOrder = farmerHireRepository.save(farmerHire);

            if (savedOrder != null) {
                response.setMessage("Order was " + actionPerformed + " successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to " + actionPerformed + " Order.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

    public FarmerHireGetResponse getFarmerHireByUserId(int userID) {
        FarmerHireGetResponse response = new FarmerHireGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerHire> farmerHireList = farmerHireRepository.findByUser_UserID(userID);

            if (farmerHireList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerHireDto> farmerHireDtoList = farmerHireList.stream()
                    .map(farmerHire -> {
                        FarmerHireDto dto = new FarmerHireDto();
                        dto.setOrderID(farmerHire.getOrderID());
                        dto.setName(farmerHire.getName());
                        dto.setPrice(farmerHire.getPrice());
                        dto.setQualifications(farmerHire.getQualifications());
                        dto.setRejected(farmerHire.isRejected());
                        dto.setAddedDate(farmerHire.getAddedDate());
                        dto.setYearsOfExperience(farmerHire.getYearsOfExperience());
                        dto.setActive(farmerHire.isActive());
                        dto.setConfirmed(farmerHire.isConfirmed());
                        dto.setAddedToCart(farmerHire.isAddedToCart());
                        dto.setRemovedFromCart(farmerHire.isRemovedFromCart());
                        dto.setPaid(farmerHire.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerHire.getUser().getUserID());
                        userDto.setUserEmail(farmerHire.getUser().getUserEmail());
                        userDto.setFirstName(farmerHire.getUser().getFirstName());
                        userDto.setLastName(farmerHire.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerHire.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerHiringDto trainerHiringDto = new TrainerHiringDto();
                        trainerHiringDto.setHireID(farmerHire.getTrainerHiring().getHireID());
                        trainerHiringDto.setName(farmerHire.getTrainerHiring().getName());
                        trainerHiringDto.setPrice(farmerHire.getTrainerHiring().getPrice());
                        trainerHiringDto.setQualifications(farmerHire.getTrainerHiring().getQualifications());
                        trainerHiringDto.setYearsOfExperience(farmerHire.getTrainerHiring().getYearsOfExperience());
                        trainerHiringDto.setAddedDate(farmerHire.getTrainerHiring().getAddedDate());
                        trainerHiringDto.setDeleted(farmerHire.getTrainerHiring().isDeleted());

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(farmerHire.getTrainerHiring().getUser().getUserID());
                        trainerUserDto.setUserEmail(farmerHire.getTrainerHiring().getUser().getUserEmail());
                        trainerUserDto.setFirstName(farmerHire.getTrainerHiring().getUser().getFirstName());
                        trainerUserDto.setLastName(farmerHire.getTrainerHiring().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(farmerHire.getTrainerHiring().getUser().getUserType()));
                        trainerHiringDto.setUser(trainerUserDto);

                        dto.setTrainerHiring(trainerHiringDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerHireGetResponse(farmerHireDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    public FarmerHireGetResponse getFarmerHireByTrainerUserId(int trainerUserID) {
        FarmerHireGetResponse response = new FarmerHireGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<FarmerHire> farmerHireList = farmerHireRepository.findByTrainerHiring_User_UserID(trainerUserID);

            if (farmerHireList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + trainerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<FarmerHireDto> farmerHireDtoList = farmerHireList.stream()
                    .map(farmerHire -> {
                        FarmerHireDto dto = new FarmerHireDto();
                        dto.setOrderID(farmerHire.getOrderID());
                        dto.setName(farmerHire.getName());
                        dto.setPrice(farmerHire.getPrice());
                        dto.setQualifications(farmerHire.getQualifications());
                        dto.setRejected(farmerHire.isRejected());
                        dto.setAddedDate(farmerHire.getAddedDate());
                        dto.setYearsOfExperience(farmerHire.getYearsOfExperience());
                        dto.setActive(farmerHire.isActive());
                        dto.setConfirmed(farmerHire.isConfirmed());
                        dto.setAddedToCart(farmerHire.isAddedToCart());
                        dto.setRemovedFromCart(farmerHire.isRemovedFromCart());
                        dto.setPaid(farmerHire.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerHire.getUser().getUserID());
                        userDto.setUserEmail(farmerHire.getUser().getUserEmail());
                        userDto.setFirstName(farmerHire.getUser().getFirstName());
                        userDto.setLastName(farmerHire.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerHire.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerHiringDto trainerHiringDto = new TrainerHiringDto();
                        trainerHiringDto.setHireID(farmerHire.getTrainerHiring().getHireID());
                        trainerHiringDto.setName(farmerHire.getTrainerHiring().getName());
                        trainerHiringDto.setPrice(farmerHire.getTrainerHiring().getPrice());
                        trainerHiringDto.setQualifications(farmerHire.getTrainerHiring().getQualifications());
                        trainerHiringDto.setYearsOfExperience(farmerHire.getTrainerHiring().getYearsOfExperience());
                        trainerHiringDto.setAddedDate(farmerHire.getTrainerHiring().getAddedDate());
                        trainerHiringDto.setDeleted(farmerHire.getTrainerHiring().isDeleted());

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(farmerHire.getTrainerHiring().getUser().getUserID());
                        trainerUserDto.setUserEmail(farmerHire.getTrainerHiring().getUser().getUserEmail());
                        trainerUserDto.setFirstName(farmerHire.getTrainerHiring().getUser().getFirstName());
                        trainerUserDto.setLastName(farmerHire.getTrainerHiring().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(farmerHire.getTrainerHiring().getUser().getUserType()));
                        trainerHiringDto.setUser(trainerUserDto);

                        dto.setTrainerHiring(trainerHiringDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerHireGetResponse(farmerHireDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }


    public FarmerHireGetResponse getFarmerHireByHireId(int hireID) {
        FarmerHireGetResponse response = new FarmerHireGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<FarmerHire> farmerHireList = farmerHireRepository.findByTrainerHiring_HireID(hireID);

            if (farmerHireList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + hireID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<FarmerHireDto> farmerHireDtoList = farmerHireList.stream()
                    .map(farmerHire -> {
                        FarmerHireDto dto = new FarmerHireDto();
                        dto.setOrderID(farmerHire.getOrderID());
                        dto.setName(farmerHire.getName());
                        dto.setPrice(farmerHire.getPrice());
                        dto.setQualifications(farmerHire.getQualifications());
                        dto.setRejected(farmerHire.isRejected());
                        dto.setAddedDate(farmerHire.getAddedDate());
                        dto.setYearsOfExperience(farmerHire.getYearsOfExperience());
                        dto.setActive(farmerHire.isActive());
                        dto.setConfirmed(farmerHire.isConfirmed());
                        dto.setAddedToCart(farmerHire.isAddedToCart());
                        dto.setRemovedFromCart(farmerHire.isRemovedFromCart());
                        dto.setPaid(farmerHire.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(farmerHire.getUser().getUserID());
                        userDto.setUserEmail(farmerHire.getUser().getUserEmail());
                        userDto.setFirstName(farmerHire.getUser().getFirstName());
                        userDto.setLastName(farmerHire.getUser().getLastName());
                        userDto.setUserType(String.valueOf(farmerHire.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerHiringDto trainerHiringDto = new TrainerHiringDto();
                        trainerHiringDto.setHireID(farmerHire.getTrainerHiring().getHireID());
                        trainerHiringDto.setName(farmerHire.getTrainerHiring().getName());
                        trainerHiringDto.setPrice(farmerHire.getTrainerHiring().getPrice());
                        trainerHiringDto.setQualifications(farmerHire.getTrainerHiring().getQualifications());
                        trainerHiringDto.setYearsOfExperience(farmerHire.getTrainerHiring().getYearsOfExperience());
                        trainerHiringDto.setAddedDate(farmerHire.getTrainerHiring().getAddedDate());
                        trainerHiringDto.setDeleted(farmerHire.getTrainerHiring().isDeleted());

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(farmerHire.getTrainerHiring().getUser().getUserID());
                        trainerUserDto.setUserEmail(farmerHire.getTrainerHiring().getUser().getUserEmail());
                        trainerUserDto.setFirstName(farmerHire.getTrainerHiring().getUser().getFirstName());
                        trainerUserDto.setLastName(farmerHire.getTrainerHiring().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(farmerHire.getTrainerHiring().getUser().getUserType()));
                        trainerHiringDto.setUser(trainerUserDto);

                        dto.setTrainerHiring(trainerHiringDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setFarmerHireGetResponse(farmerHireDtoList);
            response.setStatus("200");
            response.setMessage("Order details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving order details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    @Override
    public FarmerHireDeleteResponse DeleteFarmerHireResponse(int orderID) {
        FarmerHireDeleteResponse response = new FarmerHireDeleteResponse();

        //calculation part
        FarmerHire farmerHire;
        farmerHire = farmerHireRepository.findByOrderID(orderID);



        try {
            farmerHire.setActive(false);
            farmerHireRepository.save(farmerHire);
            response.setFarmerHireDeleteResponse(farmerHire);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerHireConfirmResponse ConfirmFarmerHireResponse(int orderID) {
        FarmerHireConfirmResponse response = new FarmerHireConfirmResponse();

        //calculation part
        FarmerHire farmerHire;
        farmerHire = farmerHireRepository.findByOrderID(orderID);



        try {
            farmerHire.setConfirmed(true);
            farmerHireRepository.save(farmerHire);
            response.setFarmerHireConfirmResponse(farmerHire);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerHireRejectResponse RejectFarmerHireResponse(int orderID) {
        FarmerHireRejectResponse response = new FarmerHireRejectResponse();

        //calculation part
        FarmerHire farmerHire;
        farmerHire = farmerHireRepository.findByOrderID(orderID);



        try {
            farmerHire.setRejected(true);
            farmerHireRepository.save(farmerHire);
            response.setFarmerHireRejectResponse(farmerHire);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerHireAddToCartResponse AddToCartFarmerHireResponse(int orderID) {
        FarmerHireAddToCartResponse response = new FarmerHireAddToCartResponse();

        //calculation part
        FarmerHire farmerHire;
        farmerHire = farmerHireRepository.findByOrderID(orderID);



        try {
            farmerHire.setAddedToCart(true);
            farmerHireRepository.save(farmerHire);
            response.setFarmerHireAddToCartResponse(farmerHire);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerHireRemovedFromCartResponse RemovedFromCartFarmerHireResponse(int orderID) {
        FarmerHireRemovedFromCartResponse response = new FarmerHireRemovedFromCartResponse();

        //calculation part
        FarmerHire farmerHire;
        farmerHire = farmerHireRepository.findByOrderID(orderID);



        try {
            farmerHire.setRemovedFromCart(true);
            farmerHireRepository.save(farmerHire);
            response.setFarmerHireRemovedFromCartResponse(farmerHire);
            response.setMessage("product Id : " + orderID + " item delete successfully");
            response.setStatus("200");
            response.setResponseCode("11000");

        }catch (Exception e){
            response.setMessage("Error delete allocate item " + e.getMessage());
            response.setResponseCode("11001");
            response.setStatus("500");

        }


        return response;
    }

    @Override
    public FarmerHirePaymentResponse PaymentFarmerHireResponse(int orderID) {
        FarmerHirePaymentResponse response = new FarmerHirePaymentResponse();

        //calculation part
        FarmerHire farmerHire;
        farmerHire = farmerHireRepository.findByOrderID(orderID);



        try {
            farmerHire.setPaid(true);
            farmerHireRepository.save(farmerHire);
            response.setFarmerHirePaymentResponse(farmerHire);
            response.setMessage("product Id : " + orderID + " item delete successfully");
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
