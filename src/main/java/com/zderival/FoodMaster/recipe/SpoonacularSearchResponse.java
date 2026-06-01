package com.zderival.FoodMaster.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpoonacularSearchResponse {
    private List<SpoonacularSearchResult> results;
}
