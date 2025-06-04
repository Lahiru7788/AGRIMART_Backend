package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerHire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerHireRepository extends JpaRepository<FarmerHire,Integer> {

    Optional<FarmerHire> findByName(String Name);
    FarmerHire findByOrderID(int orderID);
    List<FarmerHire> findByTrainerHiring_HireID(int hireID);
    List<FarmerHire> findByUser_UserID(int userID);
    List<FarmerHire> findByTrainerHiring_User_UserID(int userID);
}
