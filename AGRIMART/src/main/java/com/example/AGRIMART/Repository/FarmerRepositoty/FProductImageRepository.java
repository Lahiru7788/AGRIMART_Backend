package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FProductImageRepository extends JpaRepository<FarmerProductImage, Integer> {
    Optional<FarmerProductImage> findByFarmerProduct_ProductID(int productID);
}
