package com.example.AGRIMART.Repository.SeedsAndFertilizerRepository;

import com.example.AGRIMART.Entity.SeedsAndFertilizerEntity.SandFProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SandFProductImageRepository extends JpaRepository<SandFProductImage, Long> {
}
