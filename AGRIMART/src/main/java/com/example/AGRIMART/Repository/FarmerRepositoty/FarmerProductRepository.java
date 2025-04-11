package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerProductRepository extends JpaRepository<FarmerProduct,Integer> {
    Optional<FarmerProduct> findByProductName( String productID);
    FarmerProduct findByProductID(int productID);


}
