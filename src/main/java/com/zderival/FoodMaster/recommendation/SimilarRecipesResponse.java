package com.zderival.FoodMaster.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimilarRecipesResponse {
    private int id;
    private String title;
}
