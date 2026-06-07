package com.zderival.FoodMaster.saved;

import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/saved")
@RequiredArgsConstructor
public class SavedRecipeController {
    private final SavedRecipeService savedRecipeService;
    private final UserService userService;

    @PostMapping("/save")
    public void saveRecipe(@RequestBody SavedRecipeRequest request){
        savedRecipeService.saveRecipe(userService.extractUser().getId(), request.getSpoonacularId());
    }

    @DeleteMapping("/remove")
    public void removeRecipe(@RequestBody SavedRecipeRequest request){
        savedRecipeService.removeRecipe(userService.extractUser().getId(), request.getSpoonacularId());
    }

    @GetMapping("/getRecipes")
    public List<Recipe> getSavedRecipes(){
        return savedRecipeService.getSavedRecipes(userService.extractUser().getId());
    }


}
