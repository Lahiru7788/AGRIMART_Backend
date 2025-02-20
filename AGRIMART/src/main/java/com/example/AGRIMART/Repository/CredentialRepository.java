package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface CredentialRepository extends JpaRepository<Credentials,Integer> {
    Credentials findByUserEmail(String User);
}
