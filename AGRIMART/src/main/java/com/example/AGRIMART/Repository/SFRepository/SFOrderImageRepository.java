package com.example.AGRIMART.Repository.SFRepository;

import com.example.AGRIMART.Entity.SFEntity.SFOrderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SFOrderImageRepository extends JpaRepository<SFOrderImage, Integer> {
    Optional<SFOrderImage> findBySFOrder_OrderID(int orderID);
}

