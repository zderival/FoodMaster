package com.zderival.FoodMaster.nutrition;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NutritionProfileRepository extends JpaRepository<NutritionProfile,Long> {
    Optional<NutritionProfile> findByUserId(UUID userId);
    @Transactional
    void deleteByUserId(UUID userId);
    boolean existsNutritionProfileByUserId(UUID userId);
}
