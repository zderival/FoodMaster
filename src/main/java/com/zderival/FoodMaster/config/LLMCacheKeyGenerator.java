package com.zderival.FoodMaster.config;

import com.zderival.FoodMaster.nutrition.NutritionProfile;
import com.zderival.FoodMaster.recipe.RecipeRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

@Component("llmCacheKeyGenerator")
public class LLMCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, @Nullable Object... params) {
        RecipeRequest recipeRequest = (RecipeRequest) params[0];
        NutritionProfile profile = (NutritionProfile) params[1];

        String ingredientsParam = recipeRequest.getIngredients()
                .stream()
                .sorted().
                collect(Collectors.joining(","));
        String preferences = profile != null ? profile.getPreferences().stream().sorted().collect(Collectors.joining(",")): "no preferences" ;
        String diet = profile != null ? profile.getDiet() : "no specific diet";
        String goal = profile != null ? profile.getGoal(): "No goals";
        String allergies;

        if(profile!= null &&  !profile.getAllergies().isEmpty()){
            allergies = profile.getAllergies().stream().sorted().collect(Collectors.joining(","));
        } else if (!recipeRequest.getAllergies().isEmpty()) {
            allergies = recipeRequest.getAllergies().stream().sorted().collect(Collectors.joining(","));
        }else {
            allergies = "No allergies given";
        }

        return ingredientsParam + "|" + preferences + "|" + diet + "|" + goal + "|" + allergies;
    }
}
