package com.zderival.FoodMaster.mealplanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegenerateMealRequest {
    private String day;
    private String meal;
    private int weekNumber;
}
