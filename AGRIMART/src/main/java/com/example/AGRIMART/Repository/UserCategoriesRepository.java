package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.UserCategories;
import com.example.AGRIMART.Entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface UserCategoriesRepository extends JpaRepository<UserCategories,Integer> {
    List<UserCategories> findByUser_UserID(int userID);
}
