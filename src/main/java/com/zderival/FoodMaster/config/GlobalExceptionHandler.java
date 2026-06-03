package com.zderival.FoodMaster.config;

import com.zderival.FoodMaster.auth.InvalidCredentialsException;
import com.zderival.FoodMaster.auth.UsernameAlreadyExistsException;
import com.zderival.FoodMaster.saved.RecipeAlreadySavedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
}
