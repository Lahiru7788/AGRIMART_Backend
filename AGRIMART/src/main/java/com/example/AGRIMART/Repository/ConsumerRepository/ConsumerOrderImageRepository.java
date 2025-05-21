package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumerOrderImageRepository extends JpaRepository<ConsumerOrderImage, Integer> {
    Optional<ConsumerOrderImage> findByConsumerOrder_OrderID(int orderID);
}

