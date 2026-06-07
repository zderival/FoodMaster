package com.zderival.FoodMaster.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionProfileRequest {
    private String diet;
    private String goal;
    private List<String> allergies;
    private List<String> preferences;
}
