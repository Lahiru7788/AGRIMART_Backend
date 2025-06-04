package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiringOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerHiringOfferRepository extends JpaRepository<TrainerHiringOffer, Integer> {
    List<TrainerHiringOffer> findByTrainerHiring_HireID(int hireID);
    TrainerHiringOffer findByOfferID(int offerID);

}