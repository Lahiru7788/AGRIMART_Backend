package com.example.AGRIMART.Service.impl.ConsumerImpl;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferAddResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOffer;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerAddOrderRepository;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOfferRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOfferService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerOfferImpl implements ConsumerOfferService {

    @Autowired
    private ConsumerOfferRepository consumerOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsumerAddOrderRepository consumerAddOrderRepository;

    @Autowired
    private HttpSession session;

    @Override
    public ConsumerOfferAddResponse saveOrUpdate(ConsumerOfferDto consumerOfferDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail");
        String productName = (String) session.getAttribute("productName");

        if (username == null || username.isEmpty()) {
            ConsumerOfferAddResponse response = new ConsumerOfferAddResponse();
            response.setMessage("User is not logged in , Order is not available in the store or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        Optional<User> userOptional = userRepository.findByUserEmail(username);
        Optional<ConsumerAddOrder> orderOptional = consumerAddOrderRepository.findByProductName(productName);

        if (userOptional.isEmpty()) {
            ConsumerOfferAddResponse response = new ConsumerOfferAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        if (orderOptional.isEmpty()) {
            ConsumerOfferAddResponse response = new ConsumerOfferAddResponse();
            response.setMessage(" Product not found for the given product name.");
            return response;
        }

        User user = userOptional.get();
        ConsumerAddOrder consumerAddOrder = orderOptional.get();
        Optional<ConsumerOffer> existingOrderOptional = consumerOfferRepository.findById(consumerOfferDto.getOfferID());

        ConsumerOffer consumerOffer = existingOrderOptional.orElse(new ConsumerOffer());
        // If product exists, we update the fields; if not, we create a new one
        consumerOffer.setOfferName(consumerOfferDto.getOfferName());
        consumerOffer.setOfferDescription(consumerOfferDto.getOfferDescription());
        consumerOffer.setNewPrice(consumerOfferDto.getNewPrice());

        // Ensure the user is assigned
        consumerOffer.setUser(user);
        consumerOffer.setConsumerAddOrder(consumerAddOrder);

        // Set the flags based on business rules
        consumerOffer.setActive(true);


        ConsumerOfferAddResponse response = new ConsumerOfferAddResponse();
        try {
            // Save or update the product
            ConsumerOffer savedOffer = consumerOfferRepository.save(consumerOffer);

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

    public ConsumerOfferGetResponse GetAllConsumerOffers() {
        ConsumerOfferGetResponse response = new ConsumerOfferGetResponse();
        try {
            // Fetch all user details
            List<ConsumerOffer> consumerOfferList = consumerOfferRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<ConsumerOfferDto> consumerOfferDtoList = consumerOfferList.stream()
                    .map(consumerOffer -> {
                        ConsumerOfferDto dto = new ConsumerOfferDto();
                        dto.setOfferID(consumerOffer.getOfferID());
                        dto.setOfferName(consumerOffer.getOfferName());
                        dto.setOfferDescription(consumerOffer.getOfferDescription());
                        dto.setNewPrice(consumerOffer.getNewPrice());
                        dto.setActive(consumerOffer.isActive());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(consumerOffer.getUser().getUserID());
                        userDto.setUserEmail(consumerOffer.getUser().getUserEmail());
                        userDto.setFirstName(consumerOffer.getUser().getFirstName());
                        userDto.setLastName(consumerOffer.getUser().getLastName());
                        userDto.setUserType(String.valueOf(consumerOffer.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setConsumerOfferGetResponse(consumerOfferDtoList);
            response.setStatus("200");
            response.setMessage("Product Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving product Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }
}
