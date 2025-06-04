package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerHireDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.*;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerHire;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerHireRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerHiringRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerHireService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerHireImpl implements ConsumerHireService {

    @Autowired
    private ConsumerHireRepository consumerHireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerHiringRepository trainerHiringRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerHireAddResponse saveOrUpdate(ConsumerHireDto consumerHireDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productID = (String) session.getAttribute("productID");

        if (username == null || username.isEmpty()) {
            ConsumerHireAddResponse response = new ConsumerHireAddResponse();
            response.setMessage("User is not logged in, Product is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<TrainerHiring> productOptional = trainerHiringRepository.findByHireID(consumerHireDto.getHireID());


        if (userOptional.isEmpty()) {
            ConsumerHireAddResponse response = new ConsumerHireAddResponse();
            response.setMessage("User not found for the given username.");
            response.setStatus("404"); // Not Found
            return response;
        }

        if (productOptional.isEmpty()) {
            ConsumerHireAddResponse response = new ConsumerHireAddResponse();
            response.setMessage("Product not found for the given product name.");
            response.setStatus("404"); // Not Found
            return response;
        }


        User user = userOptional.get();
        TrainerHiring trainerHiring = productOptional.get();

        // Check if there's an existing offer for this product
        Optional<ConsumerHire> existingOrders = consumerHireRepository.findById(consumerHireDto.getOrderID());

        ConsumerHire consumerHire;
        String actionPerformed;

        if (!existingOrders.isEmpty()) {
            // Update existing offer (use the first one if multiple exist)
            consumerHire = existingOrders.get();
            actionPerformed = "updated";
        } else {
            // Create new offer
            consumerHire = new ConsumerHire();
            consumerHire.setUser(user);
            consumerHire.setTrainerHiring(trainerHiring);
            actionPerformed = "added";
        }

        // Update fields
        consumerHire.setName(consumerHireDto.getName());
        consumerHire.setPrice(consumerHireDto.getPrice());
        consumerHire.setQualifications(consumerHireDto.getQualifications());
        consumerHire.setYearsOfExperience(consumerHireDto.getYearsOfExperience());
        consumerHire.setAddedDate(consumerHireDto.getAddedDate());
        consumerHire.setActive(true);
        consumerHire.setUser(user);
        consumerHire.setTrainerHiring(trainerHiring);
        consumerHire.setConfirmed(false);
        consumerHire.setRejected(false);
        consumerHire.setAddedToCart(false);
        consumerHire.setRemovedFromCart(false);
        consumerHire.setPaid(false);

        ConsumerHireAddResponse response = new ConsumerHireAddResponse();
        try {
            // Save or update the offer
            ConsumerHire savedOrder = consumerHireRepository.save(consumerHire);

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

    public ConsumerHireGetResponse getConsumerHireByUserId(int userID) {
        ConsumerHireGetResponse response = new ConsumerHireGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerHire> consumerHireList = consumerHireRepository.findByUser_UserID(userID);

            if (consumerHireList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerHireDto> consumerHireDtoList = consumerHireList.stream()
                    .map(consumerHire -> {
                        ConsumerHireDto dto = new ConsumerHireDto();
                        dto.setOrderID(consumerHire.getOrderID());
                        dto.setName(consumerHire.getName());
                        dto.setPrice(consumerHire.getPrice());
                        dto.setQualifications(consumerHire.getQualifications());
                        dto.setRejected(consumerHire.isRejected());
                        dto.setAddedDate(consumerHire.getAddedDate());
                        dto.setYearsOfExperience(consumerHire.getYearsOfExperience());
                        dto.setActive(consumerHire.isActive());
                        dto.setConfirmed(consumerHire.isConfirmed());
                        dto.setAddedToCart(consumerHire.isAddedToCart());
                        dto.setRemovedFromCart(consumerHire.isRemovedFromCart());
                        dto.setPaid(consumerHire.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerHire.getUser().getUserID());
                        userDto.setUserEmail(consumerHire.getUser().getUserEmail());
                        userDto.setFirstName(consumerHire.getUser().getFirstName());
                        userDto.setLastName(consumerHire.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerHire.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerHiringDto trainerHiringDto = new TrainerHiringDto();
                        trainerHiringDto.setHireID(consumerHire.getTrainerHiring().getHireID());
                        trainerHiringDto.setName(consumerHire.getTrainerHiring().getName());
                        trainerHiringDto.setPrice(consumerHire.getTrainerHiring().getPrice());
                        trainerHiringDto.setQualifications(consumerHire.getTrainerHiring().getQualifications());
                        trainerHiringDto.setYearsOfExperience(consumerHire.getTrainerHiring().getYearsOfExperience());
                        trainerHiringDto.setAddedDate(consumerHire.getTrainerHiring().getAddedDate());
                        trainerHiringDto.setDeleted(consumerHire.getTrainerHiring().isDeleted());

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(consumerHire.getTrainerHiring().getUser().getUserID());
                        trainerUserDto.setUserEmail(consumerHire.getTrainerHiring().getUser().getUserEmail());
                        trainerUserDto.setFirstName(consumerHire.getTrainerHiring().getUser().getFirstName());
                        trainerUserDto.setLastName(consumerHire.getTrainerHiring().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(consumerHire.getTrainerHiring().getUser().getUserType()));
                        trainerHiringDto.setUser(trainerUserDto);

                        dto.setTrainerHiring(trainerHiringDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerHireGetResponse(consumerHireDtoList);
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

    public ConsumerHireGetResponse getConsumerHireByTrainerUserId(int trainerUserID) {
        ConsumerHireGetResponse response = new ConsumerHireGetResponse();
        try {
            // Find orders where the farmer product is related to the given farmer userID
            List<ConsumerHire> consumerHireList = consumerHireRepository.findByTrainerHiring_User_UserID(trainerUserID);

            if (consumerHireList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No orders found for farmer ID: " + trainerUserID);
                response.setResponseCode("1602");
                return response;
            }

            // Map ConsumerOrder entities to DTOs
            List<ConsumerHireDto> consumerHireDtoList = consumerHireList.stream()
                    .map(consumerHire -> {
                        ConsumerHireDto dto = new ConsumerHireDto();
                        dto.setOrderID(consumerHire.getOrderID());
                        dto.setName(consumerHire.getName());
                        dto.setPrice(consumerHire.getPrice());
                        dto.setQualifications(consumerHire.getQualifications());
                        dto.setRejected(consumerHire.isRejected());
                        dto.setAddedDate(consumerHire.getAddedDate());
                        dto.setYearsOfExperience(consumerHire.getYearsOfExperience());
                        dto.setActive(consumerHire.isActive());
                        dto.setConfirmed(consumerHire.isConfirmed());
                        dto.setAddedToCart(consumerHire.isAddedToCart());
                        dto.setRemovedFromCart(consumerHire.isRemovedFromCart());
                        dto.setPaid(consumerHire.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerHire.getUser().getUserID());
                        userDto.setUserEmail(consumerHire.getUser().getUserEmail());
                        userDto.setFirstName(consumerHire.getUser().getFirstName());
                        userDto.setLastName(consumerHire.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerHire.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerHiringDto trainerHiringDto = new TrainerHiringDto();
                        trainerHiringDto.setHireID(consumerHire.getTrainerHiring().getHireID());
                        trainerHiringDto.setName(consumerHire.getTrainerHiring().getName());
                        trainerHiringDto.setPrice(consumerHire.getTrainerHiring().getPrice());
                        trainerHiringDto.setQualifications(consumerHire.getTrainerHiring().getQualifications());
                        trainerHiringDto.setYearsOfExperience(consumerHire.getTrainerHiring().getYearsOfExperience());
                        trainerHiringDto.setAddedDate(consumerHire.getTrainerHiring().getAddedDate());
                        trainerHiringDto.setDeleted(consumerHire.getTrainerHiring().isDeleted());

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(consumerHire.getTrainerHiring().getUser().getUserID());
                        trainerUserDto.setUserEmail(consumerHire.getTrainerHiring().getUser().getUserEmail());
                        trainerUserDto.setFirstName(consumerHire.getTrainerHiring().getUser().getFirstName());
                        trainerUserDto.setLastName(consumerHire.getTrainerHiring().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(consumerHire.getTrainerHiring().getUser().getUserType()));
                        trainerHiringDto.setUser(trainerUserDto);

                        dto.setTrainerHiring(trainerHiringDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerHireGetResponse(consumerHireDtoList);
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


    public ConsumerHireGetResponse getConsumerHireByHireId(int hireID) {
        ConsumerHireGetResponse response = new ConsumerHireGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<ConsumerHire> consumerHireList = consumerHireRepository.findByTrainerHiring_HireID(hireID);

            if (consumerHireList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No products found for user ID: " + hireID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<ConsumerHireDto> consumerHireDtoList = consumerHireList.stream()
                    .map(consumerHire -> {
                        ConsumerHireDto dto = new ConsumerHireDto();
                        dto.setOrderID(consumerHire.getOrderID());
                        dto.setName(consumerHire.getName());
                        dto.setPrice(consumerHire.getPrice());
                        dto.setQualifications(consumerHire.getQualifications());
                        dto.setRejected(consumerHire.isRejected());
                        dto.setAddedDate(consumerHire.getAddedDate());
                        dto.setYearsOfExperience(consumerHire.getYearsOfExperience());
                        dto.setActive(consumerHire.isActive());
                        dto.setConfirmed(consumerHire.isConfirmed());
                        dto.setAddedToCart(consumerHire.isAddedToCart());
                        dto.setRemovedFromCart(consumerHire.isRemovedFromCart());
                        dto.setPaid(consumerHire.isPaid());



                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerHire.getUser().getUserID());
                        userDto.setUserEmail(consumerHire.getUser().getUserEmail());
                        userDto.setFirstName(consumerHire.getUser().getFirstName());
                        userDto.setLastName(consumerHire.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerHire.getUser().getUserType()));
                        dto.setUser(userDto);

                        TrainerHiringDto trainerHiringDto = new TrainerHiringDto();
                        trainerHiringDto.setHireID(consumerHire.getTrainerHiring().getHireID());
                        trainerHiringDto.setName(consumerHire.getTrainerHiring().getName());
                        trainerHiringDto.setPrice(consumerHire.getTrainerHiring().getPrice());
                        trainerHiringDto.setQualifications(consumerHire.getTrainerHiring().getQualifications());
                        trainerHiringDto.setYearsOfExperience(consumerHire.getTrainerHiring().getYearsOfExperience());
                        trainerHiringDto.setAddedDate(consumerHire.getTrainerHiring().getAddedDate());
                        trainerHiringDto.setDeleted(consumerHire.getTrainerHiring().isDeleted());

                        UserDto trainerUserDto = new UserDto();
                        trainerUserDto.setUserID(consumerHire.getTrainerHiring().getUser().getUserID());
                        trainerUserDto.setUserEmail(consumerHire.getTrainerHiring().getUser().getUserEmail());
                        trainerUserDto.setFirstName(consumerHire.getTrainerHiring().getUser().getFirstName());
                        trainerUserDto.setLastName(consumerHire.getTrainerHiring().getUser().getLastName());
                        trainerUserDto.setUserType(String.valueOf(consumerHire.getTrainerHiring().getUser().getUserType()));
                        trainerHiringDto.setUser(trainerUserDto);

                        dto.setTrainerHiring(trainerHiringDto);

                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerHireGetResponse(consumerHireDtoList);
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
    public ConsumerHireDeleteResponse DeleteConsumerHireResponse(int orderID) {
        ConsumerHireDeleteResponse response = new ConsumerHireDeleteResponse();

        //calculation part
        ConsumerHire consumerHire;
        consumerHire = consumerHireRepository.findByOrderID(orderID);



        try {
            consumerHire.setActive(false);
            consumerHireRepository.save(consumerHire);
            response.setConsumerHireDeleteResponse(consumerHire);
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
    public ConsumerHireConfirmResponse ConfirmConsumerHireResponse(int orderID) {
        ConsumerHireConfirmResponse response = new ConsumerHireConfirmResponse();

        //calculation part
        ConsumerHire consumerHire;
        consumerHire = consumerHireRepository.findByOrderID(orderID);



        try {
            consumerHire.setConfirmed(true);
            consumerHireRepository.save(consumerHire);
            response.setConsumerHireConfirmResponse(consumerHire);
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
    public ConsumerHireRejectResponse RejectConsumerHireResponse(int orderID) {
        ConsumerHireRejectResponse response = new ConsumerHireRejectResponse();

        //calculation part
        ConsumerHire consumerHire;
        consumerHire = consumerHireRepository.findByOrderID(orderID);



        try {
            consumerHire.setRejected(true);
            consumerHireRepository.save(consumerHire);
            response.setConsumerHireRejectResponse(consumerHire);
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
    public ConsumerHireAddToCartResponse AddToCartConsumerHireResponse(int orderID) {
        ConsumerHireAddToCartResponse response = new ConsumerHireAddToCartResponse();

        //calculation part
        ConsumerHire consumerHire;
        consumerHire = consumerHireRepository.findByOrderID(orderID);



        try {
                consumerHire.setAddedToCart(true);
                consumerHireRepository.save(consumerHire);
                response.setConsumerHireAddToCartResponse(consumerHire);
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
    public ConsumerHireRemovedFromCartResponse RemovedFromCartConsumerHireResponse(int orderID) {
        ConsumerHireRemovedFromCartResponse response = new ConsumerHireRemovedFromCartResponse();

        //calculation part
        ConsumerHire consumerHire;
        consumerHire = consumerHireRepository.findByOrderID(orderID);



        try {
            consumerHire.setRemovedFromCart(true);
            consumerHireRepository.save(consumerHire);
            response.setConsumerHireRemovedFromCartResponse(consumerHire);
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
    public ConsumerHirePaymentResponse PaymentConsumerHireResponse(int orderID) {
        ConsumerHirePaymentResponse response = new ConsumerHirePaymentResponse();

        //calculation part
        ConsumerHire consumerHire;
        consumerHire = consumerHireRepository.findByOrderID(orderID);



        try {
            consumerHire.setPaid(true);
            consumerHireRepository.save(consumerHire);
            response.setConsumerHirePaymentResponse(consumerHire);
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
