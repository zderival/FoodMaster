package com.zderival.FoodMaster.mealplanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegenerateDayRequest {
    private int weekNumber;
    private String day;
}
