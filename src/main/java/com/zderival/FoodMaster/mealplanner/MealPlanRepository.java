package com.zderival.FoodMaster.mealplanner;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MealPlanRepository extends JpaRepository<MealPlan,Long> {
    Optional<MealPlan> findByUserId(UUID userId);
    @Transactional
    void deleteByUserId(UUID userId);
}
