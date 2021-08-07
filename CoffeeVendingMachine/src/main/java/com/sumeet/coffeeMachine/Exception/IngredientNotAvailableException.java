package com.sumeet.coffeeMachine.Exception;

public class IngredientNotAvailableException extends RuntimeException {
    public IngredientNotAvailableException(String ingredientName) {
        throw new RuntimeException(String.format("Ingredient %s is not available ", ingredientName));
    }
}
