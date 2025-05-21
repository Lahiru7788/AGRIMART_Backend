package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupermarketSeedsOrderImageRepository extends JpaRepository<SupermarketSeedsOrderImage, Integer> {
    Optional<SupermarketSeedsOrderImage> findBySupermarketSeedsOrder_OrderID(int orderID);
}
