package com.zderival.FoodMaster.recipe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private int spoonacularId;
    private String title;

    private List<Ingredient> ingredients;
    private String instructions;
    private List<String> cuisines;
    private int readyInMinutes;

    private List<String> dietaryInfo;

   // private SpoonacularNutrition spoonacularNutrition;
    private Nutrition nutrition;
}
