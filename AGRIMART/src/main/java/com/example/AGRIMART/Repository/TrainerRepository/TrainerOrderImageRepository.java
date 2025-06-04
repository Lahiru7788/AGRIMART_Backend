package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerOrderImageRepository extends JpaRepository<TrainerOrderImage, Integer> {
    Optional<TrainerOrderImage> findByTrainerOrder_OrderID(int orderID);
}
