package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerSeedsOrderImageRepository extends JpaRepository<FarmerSeedsOrderImage, Integer> {
    Optional<FarmerSeedsOrderImage> findByFarmerSeedsOrder_OrderID(int orderID);
}