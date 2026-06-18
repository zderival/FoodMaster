package com.zderival.FoodMaster.llm;

public class AiGeneratorFailedException extends RuntimeException {
    public AiGeneratorFailedException(String message) {
        super(message);
    }
}
