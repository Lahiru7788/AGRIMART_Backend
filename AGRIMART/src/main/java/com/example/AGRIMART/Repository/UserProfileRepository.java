package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}


