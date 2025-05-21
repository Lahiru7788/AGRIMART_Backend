package com.example.AGRIMART.Service.impl.SupermarketImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOfferDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferGetResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOfferAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOfferGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOffer;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOffer;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOfferRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketAddOrderRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketOfferRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOfferService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOfferService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupermarketOfferImpl implements SupermarketOfferService {

    @Autowired
    private SupermarketOfferRepository supermarketOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupermarketAddOrderRepository supermarketAddOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public SupermarketOfferAddResponse saveOrUpdate(SupermarketOfferDto supermarketOfferDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            SupermarketOfferAddResponse response = new SupermarketOfferAddResponse();
            response.setMessage("User is not logged in , Order is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<SupermarketAddOrder> orderOptional = supermarketAddOrderRepository.findByProductName(productName);

        if (userOptional.isEmpty()) {
            SupermarketOfferAddResponse response = new SupermarketOfferAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            SupermarketOfferAddResponse response = new SupermarketOfferAddResponse();
            response.setMessage(" Product not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        SupermarketAddOrder supermarketAddOrder = orderOptional.get();
        List<SupermarketOffer> existingOffers = supermarketOfferRepository.findBySupermarketAddOrder_OrderID(supermarketAddOrder.getOrderID());

        SupermarketOffer supermarketOffer;
        String actionPerformed;

        if (!existingOffers.isEmpty()) {
            supermarketOffer = existingOffers.get(0);
            actionPerformed = "updated";
        } else {
            supermarketOffer = new SupermarketOffer();
            supermarketOffer.setUser(user);
            supermarketOffer.setSupermarketAddOrder(supermarketAddOrder);
            actionPerformed = "added";
        }
        // If product exists, we update the fields; if not, we create a new one
        supermarketOffer.setOfferName(supermarketOfferDto.getOfferName());
        supermarketOffer.setOfferDescription(supermarketOfferDto.getOfferDescription());
        supermarketOffer.setNewPrice(supermarketOfferDto.getNewPrice());

        // Ensure the user is assigned
        supermarketOffer.setUser(user);
        supermarketOffer.setSupermarketAddOrder(supermarketAddOrder);

        // Set the flags based on business rules
        supermarketOffer.setActive(true);


        SupermarketOfferAddResponse response = new SupermarketOfferAddResponse();
        try {
            // Save or update the product
            SupermarketOffer savedOffer = supermarketOfferRepository.save(supermarketOffer);

            if (savedOffer != null) {
                response.setMessage("Offer was saved/updated successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to save/update offer.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }

//    public ConsumerOfferGetResponse GetAllConsumerOffers() {
//        ConsumerOfferGetResponse response = new ConsumerOfferGetResponse();
//        try {
//            // Fetch all user details
//            List<ConsumerOffer> consumerOfferList = consumerOfferRepository.findAll();
//
//            // Map UserDetails entities to a simplified DTO without sensitive data
//            List<ConsumerOfferDto> consumerOfferDtoList = consumerOfferList.stream()
//                    .map(consumerOffer -> {
//                        ConsumerOfferDto dto = new ConsumerOfferDto();
//                        dto.setOfferID(consumerOffer.getOfferID());
//                        dto.setOfferName(consumerOffer.getOfferName());
//                        dto.setOfferDescription(consumerOffer.getOfferDescription());
//                        dto.setNewPrice(consumerOffer.getNewPrice());
//                        dto.setActive(consumerOffer.isActive());
//
//                        // Map nested user information without credentials
//                        UserDto userDto = new UserDto();
//                        userDto.setUserID(consumerOffer.getUser().getUserID());
//                        userDto.setUserEmail(consumerOffer.getUser().getUserEmail());
//                        userDto.setFirstName(consumerOffer.getUser().getFirstName());
//                        userDto.setLastName(consumerOffer.getUser().getLastName());
//                        userDto.setUserType(String.valueOf(consumerOffer.getUser().getUserType()));
//
//
//                        dto.setUser(userDto);
//                        return dto;
//                    })
//                    .collect(Collectors.toList());
//
//            response.setConsumerOfferGetResponse(consumerOfferDtoList);
//            response.setStatus("200");
//            response.setMessage("Product Details retrieved successfully");
//            response.setResponseCode("1600");
//
//        } catch (Exception e) {
//            response.setStatus("500");
//            response.setMessage("Error retrieving product Details: " + e.getMessage());
//            response.setResponseCode("1601");
//        }
//
//        return response;
//    }

    public SupermarketOfferGetResponse getSupermarketAddOffersByOrderId(int orderID) {
        SupermarketOfferGetResponse response = new SupermarketOfferGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<SupermarketOffer> supermarketOfferList = supermarketOfferRepository.findBySupermarketAddOrder_OrderID(orderID);

            if (supermarketOfferList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No offers found for order ID: " + orderID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<SupermarketOfferDto> supermarketOfferDtoList = supermarketOfferList.stream()
                    .map(supermarketOffer -> {
                        SupermarketOfferDto dto = new SupermarketOfferDto();
                        dto.setOfferID(supermarketOffer.getOfferID());
                        dto.setOfferName(supermarketOffer.getOfferName());
                        dto.setOfferDescription(supermarketOffer.getOfferDescription());
                        dto.setNewPrice(supermarketOffer.getNewPrice());
                        dto.setActive(supermarketOffer.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(supermarketOffer.getUser().getUserID());
                        userDto.setUserEmail(supermarketOffer.getUser().getUserEmail());
                        userDto.setFirstName(supermarketOffer.getUser().getFirstName());
                        userDto.setLastName(supermarketOffer.getUser().getLastName());
                        userDto.setUserType(String.valueOf(supermarketOffer.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setSupermarketOfferGetResponse(supermarketOfferDtoList);
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
}
