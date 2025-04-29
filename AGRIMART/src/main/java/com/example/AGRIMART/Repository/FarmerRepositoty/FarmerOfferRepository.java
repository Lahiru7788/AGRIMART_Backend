package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerOfferRepository extends JpaRepository<FarmerOffer, Integer> {
    List<FarmerOffer> findByFarmerProduct_ProductID(int productID);
    FarmerOffer findByOfferID(int offerID);

}
