package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupermarketOrderImageRepository extends JpaRepository<SupermarketOrderImage, Integer> {
    Optional<SupermarketOrderImage> findBySupermarketOrder_OrderID(int orderID);
}