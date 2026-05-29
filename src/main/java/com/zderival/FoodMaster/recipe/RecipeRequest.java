package com.zderival.FoodMaster.recipe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
    private List<String> ingredients;
    private String cuisine;
    private String diet;
    private List<String> preferences;
    private List<String> allergies;

}
