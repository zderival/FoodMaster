package com.zderival.FoodMaster.mealplanner;
import com.zderival.FoodMaster.llm.LLMService;
import com.zderival.FoodMaster.nutrition.NutritionProfile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final MealPlanRepository mealPlanRepository;
    private final LLMService llmService;
    private final DayPlanRepository dayPlanRepository;
    @Lazy
    @Autowired
    private MealPlanService self;

    public MealPlan generateMealPlan(UUID userId, NutritionProfile profile, int weeks){
        String preferences = profile != null ? String.join(",", profile.getPreferences()) : "no preferences" ;
        String diet = profile != null ? profile.getDiet() : "no specific diet";
        String goal = profile != null ? profile.getGoal(): "No goals";
        String allergies;

        if(profile!= null &&  !profile.getAllergies().isEmpty()){
            allergies = String.join(",", profile.getAllergies());
        }else {
            allergies = "No allergies given";
        }
        String prompt = String.format("""
You are an expert nutritionist and chef. Generate a %s-week meal plan for a user with the following profile:
- Diet: %s
- Allergies: %s (strictly exclude any ingredients containing these)
- Preferences: %s
- Goal: %s

Generate all 7 days for each week (Monday through Sunday) with breakfast, lunch, and dinner for each day.
Return ONLY raw JSON, no markdown, no code blocks, no additional text. Use this exact structure:
{
  "weeks": [
    {
      "weekNumber": 1,
      "days": [
        {
          "day": "Monday",
          "breakfast": "meal name here",
          "lunch": "meal name here",
          "dinner": "meal name here"
        }
      ]
    }
  ]
}""",weeks,diet,allergies,preferences,goal);
        MealPlan mealPlan = llmService.generateMealPlan(prompt);
        mealPlan.setUserId(userId);
        mealPlan.setNumberOfWeeks(weeks);
        mealPlanRepository.deleteByUserId(userId);
        mealPlanRepository.save(mealPlan);
        return mealPlan;
    }
    public MealPlan getMealPlan(UUID userId){
        return mealPlanRepository.findByUserId(userId)
                .orElseThrow(() -> new MealPlanNotFoundException("No meal plan found."));
    }
    @Transactional
    public MealPlan regenerateDay(UUID ussrId, int week, String day, NutritionProfile profile){
        MealPlan userMealPlan = self.getMealPlan(ussrId);
        for(WeekPlan userWeek: userMealPlan.getWeeks()){
            if (userWeek.getWeekNumber() == week){
                for (DayPlan userDay: userWeek.getDays()){
                    if (userDay.getDay().equals(day)){
                        String preferences = profile != null ? String.join(",", profile.getPreferences()) : "no preferences" ;
                        String diet = profile != null ? profile.getDiet() : "no specific diet";
                        String goal = profile != null ? profile.getGoal(): "No goals";
                        String allergies;

                        if(profile!= null &&  !profile.getAllergies().isEmpty()){
                            allergies = String.join(",", profile.getAllergies());
                        }else {
                            allergies = "No allergies given";
                        }

                        String prompt = String.format("""
                                You are an expert nutritionist and chef.
                                Generate a set of meals for the day that consist of breakfast, lunch, and dinner.
                                These set of meals are based on the following profile:
                                - Diet: %s
                                - Allergies: %s (strictly exclude any ingredients containing these)
                                - Preferences: %s
                                - Goal: %s
                                These meals are set for %s and are to be returned ONLY raw JSON, 
                                no markdown, no code blocks, no additional text. 
                                Use this exact structure:
                                {
                                  "day": "Monday",
                                  "breakfast": "meal name here",
                                  "lunch": "meal name here",
                                  "dinner": "meal name here"
                                }
                                """,diet,allergies,preferences,goal,day);
                        DayPlan dayPlan = llmService.generateDayPlan(prompt);
                        userDay.setBreakfast(dayPlan.getBreakfast());
                        userDay.setLunch(dayPlan.getLunch());
                        userDay.setDinner(dayPlan.getDinner());
                        dayPlanRepository.save(userDay);
                    }
                }
            }
        }
        return userMealPlan;
    }

    public MealPlan regenerateMeal(UUID userId, int week, String day, String meal,NutritionProfile profile){
        MealPlan userMealPlan = getMealPlan(userId);
        for(WeekPlan userWeek: userMealPlan.getWeeks()){
            if (userWeek.getWeekNumber() == week){
                for (DayPlan userDay: userWeek.getDays()){
                    if (userDay.getDay().equals(day)){
                        String preferences = profile != null ? String.join(",", profile.getPreferences()) : "no preferences" ;
                        String diet = profile != null ? profile.getDiet() : "no specific diet";
                        String goal = profile != null ? profile.getGoal(): "No goals";
                        String allergies;

                        if(profile!= null &&  !profile.getAllergies().isEmpty()){
                            allergies = String.join(",", profile.getAllergies());
                        }else {
                            allergies = "No allergies given";
                        }

                        String prompt = String.format("""
                                You are an expert nutritionist and chef.
                                Generate a meal for %s.
                                This meal should adhere to the criteria for the following profile:
                                - Diet: %s
                                - Allergies: %s (strictly exclude any ingredients containing these)
                                - Preferences: %s
                                - Goal: %s
                                Return ONLY the meal name as plain text. No JSON, no markdown, no quotes, no extra text.
                                """,meal,diet,allergies,preferences,goal);
                        String mealChange = llmService.generateMeal(prompt);
                        switch (meal.toLowerCase()){
                            case "breakfast":
                                userDay.setBreakfast(mealChange);
                                break;
                            case "lunch":
                                userDay.setLunch(mealChange);
                                break;
                            case "dinner":
                                userDay.setDinner(mealChange);
                                break;
                            default:
                                break;
                        }
                        dayPlanRepository.save(userDay);
                    }
                }
            }
        }
        return userMealPlan;
    }

}
