package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.SupermarketEntity.SAddOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SAddOrderImageRepository extends JpaRepository<SAddOrderImage, Integer> {
    Optional<SAddOrderImage> findBySupermarketAddOrder_OrderID(int orderID);
}
