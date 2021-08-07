package com.sumeet.coffeeMachine.Exception;

public class IngredientNotAvailableInSufficientAmountException extends RuntimeException{

    public IngredientNotAvailableInSufficientAmountException(String ingredientName) {
        throw new RuntimeException(String.format("Ingredient %s is not available in sufficient amount ", ingredientName));
    }
}
