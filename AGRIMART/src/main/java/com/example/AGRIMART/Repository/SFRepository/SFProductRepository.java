package com.example.AGRIMART.Repository.SFRepository;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SFProductRepository extends JpaRepository<SFProduct,Integer> {
    Optional<SFProduct> findByProductName(String productID);
    List<SFProduct> findByUser_UserID(int userID);
    SFProduct findByProductID(int productID);
//    List<SFProduct> findBySFProductProductID(int productID);
}
