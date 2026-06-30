package com.zderival.FoodMaster.history;

import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeHistoryService {
    private final RecipeHistoryRepository recipeHistoryRepository;
    private final RecipeService recipeService;

    public void saveRecipeID(UUID id, int spoonacularID){
        Optional<RecipeHistory> recipeHistory = recipeHistoryRepository.findByUserIdAndSpoonacularId(id,spoonacularID);
        if(recipeHistory.isPresent()){
            recipeHistory.get().setTimestamp(LocalDateTime.now());
            recipeHistoryRepository.save(recipeHistory.get());
        }else {
            RecipeHistory recipe = new RecipeHistory();
            recipe.setUserId(id);
            recipe.setSpoonacularId(spoonacularID);
            recipe.setTimestamp(LocalDateTime.now());
            recipeHistoryRepository.save(recipe);
        }
    }

    public Stack<Recipe> getRecipeId(UUID id){
        List<RecipeHistory> recipes = recipeHistoryRepository.findTop10ByUserIdOrderByTimestampDesc(id);
        Stack<Recipe> recipeHistories = new Stack<>();
        for (RecipeHistory recipeHistory: recipes){
            Recipe recipe = recipeService.getRecipeInformation(recipeHistory.getSpoonacularId());
            recipeHistories.push(recipe);
        }
        return recipeHistories;
    }

}
