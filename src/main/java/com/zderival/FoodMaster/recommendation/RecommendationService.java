package com.zderival.FoodMaster.recommendation;

import com.zderival.FoodMaster.nutrition.NutritionProfile;
import com.zderival.FoodMaster.nutrition.NutritionProfileService;
import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.recipe.RecipeService;
import com.zderival.FoodMaster.saved.SavedRecipe;
import com.zderival.FoodMaster.saved.SavedRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final NutritionProfileService nutritionProfileService;
    private final RestClient restClient;
    private final SavedRecipeRepository savedRecipeRepository;
    private final RecipeService recipeService;
    @Value("${spoonacular.api.key}")
    private String spoonacular_api_key;
    record ScoredRecipe(SavedRecipe recipe, double score) {}

    public List<Recipe> getRecommendations(UUID userID){
        List<SavedRecipe> savedRecipes = savedRecipeRepository.findAllByUserId(userID);
        if (savedRecipes.isEmpty()){return List.of();}
        NutritionProfile nutritionProfile = nutritionProfileService.getProfileOrNull(userID);
        PriorityQueue<ScoredRecipe> recipes = new PriorityQueue<>((a, b) -> Double.compare(b.score(), a.score()));

        for(SavedRecipe recipe: savedRecipes){
            ScoredRecipe scoredRecipe = new  ScoredRecipe(recipe,scoreRecipes(recipe,nutritionProfile));
            recipes.add(scoredRecipe);
        }
        ScoredRecipe top_recipe = recipes.poll();
        List<SimilarRecipesResponse> recommendations = restClient.get()
                .uri("https://api.spoonacular.com/recipes/{id}/similar?number=10&apiKey={apiKey}",
                        top_recipe.recipe().getSpoonacularId(),spoonacular_api_key)
                .retrieve()
                .body(new ParameterizedTypeReference<List<SimilarRecipesResponse>>() {});
        List<Recipe> recipesList = new ArrayList<>();
        for(SimilarRecipesResponse recipe: recommendations){
            Recipe recipeInfo = recipeService.getRecipeInformation(recipe.getId());
            recipesList.add(recipeInfo);
        }
        return recipesList;
    }

    private double scoreRecipes(SavedRecipe recipe, NutritionProfile profile){
        if (profile == null){return 0;}

        return switch (profile.getGoal().toLowerCase()) {
            case "lose weight" -> (recipe.getFiber() / 80) - (recipe.getCalories() / 2000);
            case "build muscle" -> (recipe.getProtein() / 150) + (recipe.getCalories() / 2000);
            case "lean" -> (recipe.getProtein() / 150) - (recipe.getFat() / 100);
            case "get stronger" -> (2 * (recipe.getProtein() / 150)) + (recipe.getCalories() / 2000);
            default -> 0;
        };
    }
}
