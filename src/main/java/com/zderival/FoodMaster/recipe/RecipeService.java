package com.zderival.FoodMaster.recipe;

import com.zderival.FoodMaster.llm.LLMService;
import com.zderival.FoodMaster.nutrition.NutritionProfileService;
import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RestClient restClient;
    @Value("${spoonacular.api.key}")
    private String spoonacular_api_key;
    @Autowired
    @Lazy
    private RecipeService self;
    private final LLMService llmService;
    private final NutritionProfileService profileService;
    private final UserService userService;

    public List<SpoonacularSearchResult> searchRecipes(RecipeRequest request) {
        String ingredientsParam = String.join(",", request.getIngredients());
        String allergiesParam = String.join(",", request.getAllergies());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://api.spoonacular.com/recipes/complexSearch/")
                .queryParam("includeIngredients", ingredientsParam)
                .queryParam("addRecipeInformation", true)
                .queryParam("number", 5)
                .queryParam("apiKey", spoonacular_api_key);

        if (request.getCuisine() != null && !request.getCuisine().isEmpty()) {
            builder.queryParam("cuisine", request.getCuisine());
        }

        if (request.getDiet() != null && !request.getDiet().isEmpty()) {
            builder.queryParam("diet", request.getDiet());
        }

        if (request.getAllergies() != null && !request.getAllergies().isEmpty()) {
            builder.queryParam("intolerances", allergiesParam);
        }
        String url = builder.toUriString();
        SpoonacularSearchResponse response = restClient.get().uri(url).retrieve().body(SpoonacularSearchResponse.class);
        return response.getResults();
    }

    @Cacheable(value = "recipes", key = "#id")
    public Recipe getRecipeInformation(int id){
        SpoonacularRecipeResponse spoonacularRecipe = restClient.get()
                .uri("https://api.spoonacular.com/recipes/{id}/information/?includeNutrition=true&apiKey={apiKey}",
                id,spoonacular_api_key)
                .retrieve()
                .body(SpoonacularRecipeResponse.class);

        Nutrition nutrition = new Nutrition();
        for(SpoonacularNutrient nutrient: spoonacularRecipe.getNutrition().getNutrients()){
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
        List<String> dietaryInfo = new ArrayList<>();
        if(spoonacularRecipe.isVegetarian()){dietaryInfo.add("Vegetarian");}
        if(spoonacularRecipe.isVegan()){dietaryInfo.add("Vegan");}
        if(spoonacularRecipe.isDairyFree()){dietaryInfo.add("Dairy Free");}
        if(spoonacularRecipe.isGlutenFree()){dietaryInfo.add("Gluten Free");}

        Recipe recipeDTO = new Recipe();
        recipeDTO.setSpoonacularId(spoonacularRecipe.getSpoonacularId());
        recipeDTO.setTitle(spoonacularRecipe.getTitle());
        recipeDTO.setNutrition(nutrition);
        recipeDTO.setDietaryInfo(dietaryInfo);
        recipeDTO.setIngredients(spoonacularRecipe.getIngredients());
        recipeDTO.setCuisines(spoonacularRecipe.getCuisines());
        recipeDTO.setInstructions(spoonacularRecipe.getInstructions());
        recipeDTO.setReadyInMinutes(spoonacularRecipe.getReadyInMinutes());
        return recipeDTO;

    }

    public List<Recipe> getRecipes(RecipeRequest request){
        // Step 1: get basic results with IDs
        List<SpoonacularSearchResult> searchResults = searchRecipes(request);
        // Check if user is authenticated — anonymous users have a String principal ("anonymousUser")
        // Real authenticated users have a UserDetails object as their principal
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        if(searchResults.isEmpty() && isLoggedIn){
            return llmService.generateRecipes(request,profileService.getProfileOrNull(userService.extractUser().getId()));
        }
        // Step 2: for each result, get full details of recipe
        List<Recipe> recipes = new ArrayList<>();
        for(SpoonacularSearchResult result : searchResults){
            Recipe recipe = self.getRecipeInformation(result.getId());
            recipes.add(recipe);
        }
        return recipes;
    }
}

