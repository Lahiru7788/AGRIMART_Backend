package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.User;
//import com.example.AGRIMART.Entity.UserDetails;
//import com.example.AGRIMART.Entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserEmail(String userEmail);
//    Optional<User> findUserDetailsByUserEmail(String userEmail);
}
