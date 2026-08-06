package com.zderival.FoodMaster.config;

import com.zderival.FoodMaster.nutrition.NutritionProfile;
import com.zderival.FoodMaster.recipe.RecipeRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Component("llmCacheKeyGenerator")
public class LLMCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, @Nullable Object... params) {
        RecipeRequest recipeRequest = (RecipeRequest) params[0];
        NutritionProfile profile = (NutritionProfile) params[1];
        List<String> ingredientsList = recipeRequest.getIngredients() != null ? recipeRequest.getIngredients() : List.of();
        String ingredientsParam = ingredientsList
                .stream()
                .sorted().
                collect(Collectors.joining(","));
        String preferences = profile != null ? profile.getPreferences().stream().sorted().collect(Collectors.joining(",")): "no preferences" ;
        String diet = profile != null ? profile.getDiet() : "no specific diet";
        String goal = profile != null ? profile.getGoal(): "No goals";
        List<String> allergiesRequest = recipeRequest.getAllergies() != null ? recipeRequest.getAllergies() : List.of();
        String allergies;
        if(profile!= null &&  !profile.getAllergies().isEmpty()){
            allergies = profile.getAllergies().stream().sorted().collect(Collectors.joining(","));
        } else if (!allergiesRequest.isEmpty()) {
            allergies = allergiesRequest.stream().sorted().collect(Collectors.joining(","));
        }else {
            allergies = "No allergies given";
        }

        return ingredientsParam + "|" + preferences + "|" + diet + "|" + goal + "|" + allergies;
    }
}
