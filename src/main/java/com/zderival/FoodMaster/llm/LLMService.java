package com.zderival.FoodMaster.llm;

import com.zderival.FoodMaster.nutrition.NutritionProfile;
import com.zderival.FoodMaster.recipe.Recipe;
import com.zderival.FoodMaster.recipe.RecipeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LLMService {
    private final RestClient restClient;
    private final JsonMapper jsonMapper;
    @Value("${gemini.api.key}")
    private String gemini_api_key;

    public List<Recipe> generateRecipes(RecipeRequest request, NutritionProfile profile){
        String ingredientsParam = String.join(",", request.getIngredients());
        String preferences = profile != null ? String.join(",", profile.getPreferences()) : "no preferences" ;
        String diet = profile != null ? profile.getDiet() : "no specific diet";
        String goal = profile != null ? profile.getGoal(): "No goals";
        String allergies;

        if(profile!= null &&  !profile.getAllergies().isEmpty()){
            allergies = String.join(",", profile.getAllergies());
        } else if (!request.getAllergies().isEmpty()) {
            allergies = String.join(",", request.getAllergies());
        }else {
            allergies = "No allergies given";
        }

        String prompt = String.format("""
                You are nutrition and chef expert/master.
                Suggest 5 recipes for a user based on these guidelines:
                They have these items in their home to cook with: %s,
                They are under this diet %s,
                they are allergic to %s and are to not have any recipes containing any of the content with those allergies,
                they have additional preferences like: %s
                and the user wants recipes based on this goal: %s.
                Return exactly 5 recipes as a JSON array with no additional text, no markdown, no code blocks — raw JSON only.
                Use this exact structure:
                [ { "spoonacularId": -1, "title": "string", "ingredients": [ { "name": "string", "amount": 0.0, "unit": "string" } ], "instructions": "string", "cuisines": ["string"], "readyInMinutes": 0, "dietaryInfo": ["string"], "nutrition": { "calories": 0.0, "protein": 0.0, "fat": 0.0, "carbohydrates": 0.0, "fiber": 0.0, "sugar": 0.0, "sodium": 0.0, "cholesterol": 0.0 } } ] 
                """, ingredientsParam,diet,allergies,preferences,goal);
        GeminiPart part = new GeminiPart(prompt);
        List<GeminiPart> geminiPart = List.of(part);
        GeminiContent content = new GeminiContent(geminiPart);
        GeminiRequest geminiRequest = new GeminiRequest(List.of(content));
        GeminiResponse response = restClient.post().uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent")
                .header("x-goog-api-key", gemini_api_key)
                .body(geminiRequest).retrieve().body(GeminiResponse.class);
        GeminiCandidate firstCandidate = response.getCandidates().getFirst();
        GeminiContent geminiContent = firstCandidate.getContent();
        GeminiPart geminipart = geminiContent.getParts().getFirst();
        String text = geminipart.getText();
        List<Recipe> recipes;
        try {
            recipes = jsonMapper.readValue(text, new TypeReference<List<Recipe>>() {});
        } catch (JacksonException e){
            throw new AiGeneratorFailedException("Failed to generate recipes from AI service — please try again");
        }
        for(Recipe recipe:recipes){
            recipe.setSpoonacularId(-1);
        }
        return recipes;
    }
}
