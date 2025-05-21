package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumerSeedsOrderImageRepository extends JpaRepository<ConsumerSeedsOrderImage, Integer> {
    Optional<ConsumerSeedsOrderImage> findByConsumerSeedsOrder_OrderID(int orderID);
}