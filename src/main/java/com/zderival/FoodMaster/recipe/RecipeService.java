package com.zderival.FoodMaster.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RestClient restClient;
    @Value("${spoonacular.api.key}")
    private String spoonacular_api_key;

    public List<SpoonacularSearchResult> searchByIngredients(List<String> ingredients){
        String ingredientsParam = String.join(",", ingredients);
        return restClient.get().uri("https://api.spoonacular.com/recipes/findByIngredients?ingredients={ingredients}&number=5&apiKey={apiKey}",
                        ingredientsParam,spoonacular_api_key).retrieve().body(new ParameterizedTypeReference<List<SpoonacularSearchResult>>() {
                });
    }

    public Recipe getRecipeInformation(int id){
        Recipe recipe = restClient.get()
                .uri("https://api.spoonacular.com/recipes/{id}/information/?includeNutrition=true&apiKey={apiKey}",
                id,spoonacular_api_key)
                .retrieve()
                .body(Recipe.class);
        Nutrition nutrition = new Nutrition();
        for(SpoonacularNutrient nutrient: recipe.getSpoonacularNutrition().getNutrients()){
            switch (nutrient.getName()){
                case "Calories" -> nutrition.setCalories(nutrient.getAmount());
                case "Protein" -> nutrition.setProtein(nutrient.getAmount());
                case "Fat" -> nutrition.setFat(nutrient.getAmount());
                case "Carbohydrates" -> nutrition.setCarbohydrates(nutrient.getAmount());
                case "Fiber" -> nutrition.setFiber(nutrient.getAmount());
                case "Sugar" -> nutrition.setSugar(nutrient.getAmount());
                case "Sodium" -> nutrition.setSodium(nutrient.getAmount());
                case "Cholesterol" -> nutrition.setCholesterol(nutrient.getAmount());
            }
        }
        recipe.setNutrition(nutrition);
        List<String> dietaryInfo = new ArrayList<>();
        if(recipe.isVegetarian()){dietaryInfo.add("Vegetarian");}
        if(recipe.isVegan()){dietaryInfo.add("Vegan");}
        if(recipe.isDairyFree()){dietaryInfo.add("Dairy Free");}
        if(recipe.isGlutenFree()){dietaryInfo.add("Gluten Free");}
        recipe.setDietaryInfo(dietaryInfo);
        return recipe;

    }

    public List<Recipe> getRecipes(List<String> ingredients){
        // Step 1: get basic results with IDs
        List<SpoonacularSearchResult> searchResults = searchByIngredients(ingredients);
        // Step 2: for each result, get full details of recipe
        List<Recipe> recipes = new ArrayList<>();
        for(SpoonacularSearchResult result : searchResults){
            Recipe recipe = getRecipeInformation(result.getId());
            recipes.add(recipe);
        }
        return recipes;
    }
}
