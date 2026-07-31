package com.zderival.FoodMaster.config;

import com.zderival.FoodMaster.auth.InvalidCredentialsException;
import com.zderival.FoodMaster.auth.UsernameAlreadyExistsException;
import com.zderival.FoodMaster.llm.AiGeneratorFailedException;
import com.zderival.FoodMaster.mealplanner.MealPlanNotFoundException;
import com.zderival.FoodMaster.nutrition.InvalidGoalException;
import com.zderival.FoodMaster.nutrition.ProfileExistsException;
import com.zderival.FoodMaster.nutrition.ProfileNotFoundException;
import com.zderival.FoodMaster.saved.RecipeAlreadySavedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(RecipeAlreadySavedException.class)
        public ResponseEntity<String> handleRecipeAlreadySavedException(RecipeAlreadySavedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<String> handleProfileNotFoundException(ProfileNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ProfileExistsException.class)
    public ResponseEntity<String> handleProfileExistsException(ProfileExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(AiGeneratorFailedException.class)
    public ResponseEntity<String> handleAiGeneratorFailedException(AiGeneratorFailedException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(InvalidGoalException.class)
    public ResponseEntity<String> handleInvalidGoalException(InvalidGoalException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MealPlanNotFoundException.class)
    public ResponseEntity<String> handleMealPlanNotFoundException(MealPlanNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException e){
        if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Service unavailable at the moment, please try again later.");
        } else {
            return ResponseEntity.status(e.getStatusCode()).body("An error occurred with external API. " +
                    "Please try again later.");
        }    }
}
