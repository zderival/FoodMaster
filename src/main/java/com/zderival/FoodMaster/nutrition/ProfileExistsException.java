package com.zderival.FoodMaster.nutrition;

public class ProfileExistsException extends RuntimeException {
    public ProfileExistsException(String message) {
        super(message);
    }
}
