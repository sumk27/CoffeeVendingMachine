package com.sumeet.coffeeMachine;

import java.util.Objects;

public class RecipeIngredient implements Comparable<RecipeIngredient>{

    private String ingredientName;

    private Integer quantity;

    public RecipeIngredient(String ingredientName, Integer quantity) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "ingredientName=" + ingredientName +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredient)) return false;

        RecipeIngredient that = (RecipeIngredient) o;

        if (!Objects.equals(ingredientName, that.ingredientName))
            return false;
        return Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        int result = ingredientName != null ? ingredientName.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(RecipeIngredient o) {
        return this.getIngredientName().compareTo(o.getIngredientName());
    }
}
