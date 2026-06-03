package com.zderival.FoodMaster.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    @PostMapping("/search")
    public List<Recipe> getRecipes(@RequestBody RecipeRequest ingredients){
        return recipeService.getRecipes(ingredients);
    }
}
