package com.example.AGRIMART.Repository.SFRepository;

import com.example.AGRIMART.Entity.SFEntity.SFProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SFProductImageRepository extends JpaRepository<SFProductImage, Integer> {
    Optional<SFProductImage> findBySFProduct_ProductID(int productID);
}
//package com.example.AGRIMART.Repository.SFRepository;
//
//import com.example.AGRIMART.Entity.SFEntity.SFProductImage;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//
//public interface SFProductImageRepository extends JpaRepository<SFProductImage, Integer> {
//    @Query("SELECT s FROM SFProductImage s WHERE s.seedsAndFertilizerProduct.productID = :productID")
//    Optional<SFProductImage> findByProductId(@Param("productID") int productID);
//}