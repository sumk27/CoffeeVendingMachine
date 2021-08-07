package com.sumeet.coffeeMachine;

import java.util.Collections;
import java.util.List;

public class CoffeeVendingMachineExample {


    public static void main(String[] args) throws InterruptedException {
        sampleTestCase();
    }

    static void sampleTestCase() throws InterruptedException {
        Integer noOfOutLets = 4;

        Ingredient water = new Ingredient("Water", 1000, 500, 10);
        Ingredient sugarSyrup = new Ingredient("Sugar Syrup", 50, 5, 10);
        Ingredient milk = new Ingredient("Milk", 1000, 500, 10);
        Ingredient coffeeSyrup = new Ingredient("Coffee Syrup", 50, 15, 10);
        Ingredient teaLeavesSyrup = new Ingredient("Tea Leaves Syrup", 50, 15, 10);

        List<Ingredient> ingredients = List.of(water, sugarSyrup, milk, coffeeSyrup, teaLeavesSyrup);

        Beverage hotWater = new Beverage("Hot Water", new Recipe(Collections.singletonList(new RecipeIngredient(water.getName(), 200))));
        Beverage hotMilk = new Beverage("Hot Milk", new Recipe(Collections.singletonList(new RecipeIngredient(milk.getName(), 200))));

        RecipeIngredient waterRecipeIngredient = new RecipeIngredient(water.getName(), 20);
        RecipeIngredient milkRecipeIngredient = new RecipeIngredient(milk.getName(), 20);
        RecipeIngredient sugarRecipeIngredient = new RecipeIngredient(sugarSyrup.getName(), 5);
        RecipeIngredient coffeeRecipeIngredient = new RecipeIngredient(coffeeSyrup.getName(), 8);
        RecipeIngredient teaRecipeIngredient = new RecipeIngredient(teaLeavesSyrup.getName(), 8);

        RecipeIngredient greenTeaRecipeIngredient = new RecipeIngredient("Green Tea syrup", 5);

        Beverage tea = new Beverage( "Tea", new Recipe(List.of(waterRecipeIngredient, milkRecipeIngredient, sugarRecipeIngredient, teaRecipeIngredient)));
        Beverage coffee = new Beverage( "Coffee", new Recipe(List.of(waterRecipeIngredient, milkRecipeIngredient, sugarRecipeIngredient, coffeeRecipeIngredient)));
        Beverage greenTea = new Beverage( "Green Tea", new Recipe(List.of(waterRecipeIngredient, sugarRecipeIngredient, teaRecipeIngredient, greenTeaRecipeIngredient)));


        List<Beverage> beverages = List.of(hotMilk, hotWater, tea, coffee, greenTea);

        CoffeeMachine coffeeMachine = CoffeeMachine.setupCoffeeMachine(noOfOutLets, ingredients, beverages);

        Thread t1 = new Thread(() -> {
            try {
                coffeeMachine.orderBeverage("Hot Water");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                coffeeMachine.orderBeverage("Tea");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread t3 = new Thread(() -> {
            try {
                coffeeMachine. orderBeverage("Coffee");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t4 = new Thread(() -> {
            try {
                coffeeMachine.orderBeverage("Hot Milk");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t5 = new Thread(() -> {
            try {
                coffeeMachine.orderBeverage("Hot Milk");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });



        Thread t6 = new Thread(() -> {
            try {
                coffeeMachine.orderBeverage("Green Tea");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();

        System.out.println("All Served");
    }

}
