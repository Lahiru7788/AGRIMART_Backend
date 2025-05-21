package com.example.AGRIMART.Repository.SFRepository;

import com.example.AGRIMART.Entity.SFEntity.SFOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SFOfferRepository extends JpaRepository<SFOffer, Integer> {
    List<SFOffer> findBySFProduct_ProductID(int productID);
    SFOffer findByOfferID(int offerID);
}

