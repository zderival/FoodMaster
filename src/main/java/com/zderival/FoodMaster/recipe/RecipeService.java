package com.zderival.FoodMaster.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RestClient restClient;
    @Value("${spoonacular.api.key}")
    private String spoonacular_api_key;
}
