package com.sumeet.coffeeMachine;

public class Beverage {

    private final String name;

    private final Recipe recipe;


    public Beverage(String name, Recipe recipe) {
        this.name = name;
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "Beverage{" +
                "name='" + name + '\'' +
                ", recipe=" + recipe +
                '}';
    }
}
