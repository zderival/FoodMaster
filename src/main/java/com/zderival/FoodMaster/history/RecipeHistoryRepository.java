package com.zderival.FoodMaster.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeHistoryRepository extends JpaRepository<RecipeHistory, Long> {
    List<RecipeHistory> findTop10ByUserIdOrderByTimestampDesc(UUID userId);
    Optional<RecipeHistory> findByUserIdAndSpoonacularId(UUID userId, int spoonacularId);

}
