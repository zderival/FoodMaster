package com.zderival.FoodMaster.saved;

import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.user.User;
import com.zderival.FoodMaster.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/saved")
@RequiredArgsConstructor
public class SavedRecipeController {
    private final SavedRecipeService savedRecipeService;
    private final UserRepository userRepository;

    @PostMapping("/save")
    public void saveRecipe(@RequestBody SavedRecipeRequest request){
        savedRecipeService.saveRecipe(extractUser().getId(), request.getSpoonacularId());
    }

    @DeleteMapping("/remove")
    public void removeRecipe(@RequestBody SavedRecipeRequest request){
        savedRecipeService.removeRecipe(extractUser().getId(), request.getSpoonacularId());
    }

    @GetMapping("/getRecipes")
    public List<Recipe> getSavedRecipes(){
        return savedRecipeService.getSavedRecipes(extractUser().getId());
    }

    private User extractUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
