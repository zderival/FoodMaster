package com.zderival.FoodMaster.recipe;

import com.zderival.FoodMaster.history.RecipeHistoryService;
import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeHistoryService recipeHistoryService;
    private final UserService userService;

    @PostMapping("/search")
    public List<Recipe> getRecipes(@RequestBody RecipeRequest ingredients){
        return recipeService.getRecipes(ingredients);
    }
    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable int id){
        Recipe recipe = recipeService.getRecipeInformation(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        if(isLoggedIn){
            recipeHistoryService.saveRecipeID(userService.extractUser().getId(),id);
        }
        return recipe;
    }
}
