package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,Integer> {
}
