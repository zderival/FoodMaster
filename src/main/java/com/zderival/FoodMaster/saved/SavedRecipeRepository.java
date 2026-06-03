package com.zderival.FoodMaster.saved;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe,Long> {
    void deleteByUserIdAndSpoonacularId(UUID userId, int spoonacularId);
    boolean existsByUserIdAndSpoonacularId(UUID userId, int spoonacularId);
    List<SavedRecipe> findAllByUserId(UUID userId);

}
