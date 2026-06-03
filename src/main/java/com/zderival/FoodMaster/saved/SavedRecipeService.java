package com.zderival.FoodMaster.saved;

import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.recipe.RecipeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavedRecipeService {
    private final SavedRecipeRepository repository;
    private final RecipeService recipeService;

    public void saveRecipe(UUID id, int spoonacularId){
        if(repository.existsByUserIdAndSpoonacularId(id, spoonacularId))
        {throw new RecipeAlreadySavedException("This recipe is already saved to your account.");}
        SavedRecipe recipe = new SavedRecipe();
        recipe.setUserId(id);
        recipe.setSpoonacularId(spoonacularId);
        repository.save(recipe);
    }
    @Transactional
    public void removeRecipe(UUID id, int spoonacularId){
        repository.deleteByUserIdAndSpoonacularId(id,spoonacularId);
    }
    public List<Recipe> getSavedRecipes(UUID id){
        List<Recipe> recipes = new ArrayList<>();
        for (SavedRecipe recipe: repository.findAllByUserId(id)){
            Recipe recipeInfo = recipeService.getRecipeInformation(recipe.getSpoonacularId());
            recipes.add(recipeInfo);
        }
        return recipes;
    }
}
