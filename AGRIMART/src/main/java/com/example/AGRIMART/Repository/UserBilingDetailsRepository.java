package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.UserBilingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBilingDetailsRepository extends JpaRepository<UserBilingDetails,Integer> {
    List<UserBilingDetails> findByUser_UserID(int userID);
}