package com.zderival.FoodMaster.saved;

public class RecipeAlreadySavedException extends RuntimeException {
    public RecipeAlreadySavedException(String message) {
        super(message);
    }
}
