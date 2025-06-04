package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerHiringRepository extends JpaRepository<TrainerHiring,Integer> {
    Optional<TrainerHiring> findByName(String Name);
//    TrainerHiring findByHireID(int hireID);
    Optional<TrainerHiring> findByHireID(int hireID);
    List<TrainerHiring> findByUser_UserID(int userID);
}