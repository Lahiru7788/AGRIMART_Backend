package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CAddOrderImageRepository extends JpaRepository<CAddOrderImage, Integer> {
    Optional<CAddOrderImage> findByConsumerAddOrder_OrderID(int orderID);
}
