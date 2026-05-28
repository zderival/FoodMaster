package com.zderival.FoodMaster.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nutrition {
    private double calories;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double sugar;
    private double sodium;
    private double cholesterol;
}
