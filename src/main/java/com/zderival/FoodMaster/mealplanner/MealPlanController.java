package com.zderival.FoodMaster.mealplanner;

import com.zderival.FoodMaster.nutrition.NutritionProfileService;
import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mealplanner")
@RequiredArgsConstructor
public class MealPlanController {
    private final MealPlanService mealPlanService;
    private final UserService userService;
    private final NutritionProfileService nutritionProfileService;

    @PostMapping("/createPlanner")
    public MealPlan generateMealPlan(@RequestParam int weeks){
        return mealPlanService.generateMealPlan(userService.extractUser().getId(),
                nutritionProfileService.getProfileOrNull(userService.extractUser().getId()),
                weeks);
    }

    @GetMapping("/getPlanner")
    public MealPlan getMealPlan( ){
        return mealPlanService.getMealPlan(userService.extractUser().getId());
    }
    @PutMapping("/updateDay")
    public MealPlan regenerateDay(@RequestBody RegenerateDayRequest request){
       return mealPlanService.regenerateDay(userService.extractUser().getId(),
               request.getWeekNumber(),
               request.getDay(),nutritionProfileService.getProfileOrNull(userService.extractUser().getId()));
    }

    @PutMapping("/updateMeal")
    public MealPlan regenerateMeal(@RequestBody RegenerateMealRequest request){
        return mealPlanService.regenerateMeal(userService.extractUser().getId(), request.getWeekNumber(),
                request.getDay(),
                request.getMeal(),nutritionProfileService.getProfileOrNull(userService.extractUser().getId()));
    }

}
