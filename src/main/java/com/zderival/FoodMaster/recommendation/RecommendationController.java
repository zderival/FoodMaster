package com.zderival.FoodMaster.recommendation;

import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final UserService userService;

    @GetMapping("/getRecommendations")
    public List<Recipe> getRecommendations(){
        return recommendationService.getRecommendations(userService.extractUser().getId());
    }
}
