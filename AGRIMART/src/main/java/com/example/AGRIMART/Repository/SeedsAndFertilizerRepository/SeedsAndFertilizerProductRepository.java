package com.example.AGRIMART.Repository.SeedsAndFertilizerRepository;

import com.example.AGRIMART.Entity.SeedsAndFertilizerEntity.SeedsAndFertilizerProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeedsAndFertilizerProductRepository extends JpaRepository<SeedsAndFertilizerProduct,Integer> {
    Optional<SeedsAndFertilizerProduct> findByProductID(int productID);
}
