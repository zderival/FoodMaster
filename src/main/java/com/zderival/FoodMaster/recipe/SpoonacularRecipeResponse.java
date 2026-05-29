package com.zderival.FoodMaster.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpoonacularRecipeResponse {
    @JsonProperty("id")
    private int spoonacularId;
    private String title;
    @JsonProperty("extendedIngredients")
    private List<Ingredient> ingredients;
    private String instructions;
    private List<String> cuisines;
    private int readyInMinutes;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean dairyFree;
    private SpoonacularNutrition nutrition;
}
