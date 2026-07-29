package com.zderival.FoodMaster.mealplan;

import com.zderival.FoodMaster.llm.LLMService;
import com.zderival.FoodMaster.mealplanner.DayPlanRepository;
import com.zderival.FoodMaster.mealplanner.MealPlanNotFoundException;
import com.zderival.FoodMaster.mealplanner.MealPlanRepository;
import com.zderival.FoodMaster.mealplanner.MealPlanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MealPlanServiceTest{
    @InjectMocks
    private MealPlanService mealPlanService;
    @Mock
    private MealPlanRepository mealPlanRepository;
    @Mock
    private LLMService llmService;
    @Mock
    private DayPlanRepository dayPlanRepository;
    @Test
    public void getMealPlan_withNoProfile_throwsMealPlanNotFoundException(){
        when(mealPlanRepository.findByUserId(any())).thenReturn(Optional.empty());
        assertThrows(MealPlanNotFoundException.class,() -> {
            mealPlanService.getMealPlan(UUID.randomUUID());
        });
    }
}
