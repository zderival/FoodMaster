package com.zderival.FoodMaster.history;

import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Stack;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class RecipeHistoryController {
    private final UserService userService;
    private final RecipeHistoryService recipeHistoryService;

    @GetMapping("/recipes")
    public Stack<Recipe> getRecipesId(){
        return recipeHistoryService.getRecipeId(userService.extractUser().getId());
    }

}
