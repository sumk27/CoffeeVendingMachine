package com.sumeet.coffeeMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private List<RecipeIngredient> recipeIngredientList;

    public Recipe(List<RecipeIngredient> recipeIngredientList) {
        this.recipeIngredientList = getSortedRecipeIngredients(recipeIngredientList);
    }


    private ArrayList<RecipeIngredient> getSortedRecipeIngredients(List<RecipeIngredient> recipeIngredientList) {
        //Useful for locking the ingredients in specific order(ascending order by name) to avoid deadlocks
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>(recipeIngredientList);
        Collections.sort(recipeIngredients);
        return recipeIngredients;
    }

    public List<RecipeIngredient> getRecipeIngredientList() {
        return recipeIngredientList;
    }

    public void setRecipeIngredientList(List<RecipeIngredient> recipeIngredientList) {
        this.recipeIngredientList = getSortedRecipeIngredients(recipeIngredientList);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeIngredientList=" + recipeIngredientList +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;

        Recipe recipe = (Recipe) o;

        return Objects.equals(recipeIngredientList, recipe.recipeIngredientList);
    }

    @Override
    public int hashCode() {
        return recipeIngredientList != null ? recipeIngredientList.hashCode() : 0;
    }
}
