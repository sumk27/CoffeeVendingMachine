package com.sumeet.coffeeMachine;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sumeet.coffeeMachine.Exception.MachineSetupException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CoffeeVendingMachineDriver {

    private static CoffeeMachine coffeeMachine;

    public static CoffeeMachine setupCoffeeMachine(JsonObject coffeeMachineSetupInfo) {

        try {
            JsonObject machineObject = Optional.of(coffeeMachineSetupInfo.getAsJsonObject("machine"))
                    .orElseThrow(MachineSetupException::new);

            Integer noOfOutlets = fetchNoOfOutlets(machineObject).orElseThrow(MachineSetupException::new);

            List<Ingredient> ingredients = fetchTotalIngredients(machineObject);
            if (ingredients.size() == 0) {
                System.out.println("No Ingredient in the coffee machine");
            }

            List<Beverage> beverages = fetchBeveragesQuantity(machineObject);
            if (ingredients.size() == 0) {
                System.out.println("No beverages is configured in the coffee machine");
            }

            coffeeMachine = CoffeeMachine.setupCoffeeMachine(noOfOutlets, ingredients, beverages);

        } catch (RuntimeException machineSetupException) {
            System.out.println(machineSetupException.getMessage());
        }

        return coffeeMachine;
    }

    private static List<Ingredient> fetchTotalIngredients(JsonObject machineObject) throws MachineSetupException {
        JsonObject totalItems = Optional.of(machineObject.getAsJsonObject("total_items_quantity"))
                .orElseThrow(MachineSetupException::new);

        Set<String> ingredients = totalItems.keySet();

        List<Ingredient> ingredientList = new ArrayList<>(ingredients.size());

        for (String ingredient : ingredients) {
            Integer quantity = totalItems.getAsJsonPrimitive(ingredient).getAsInt();
            Integer threshold = (int) 0.1 * quantity;
            ingredientList.add(new Ingredient(ingredient, quantity, quantity, threshold));
        }

        return ingredientList;
    }

    private static List<Beverage> fetchBeveragesQuantity(JsonObject machineObject) throws MachineSetupException {
        JsonObject allBeverages = Optional.of(machineObject.getAsJsonObject("beverages"))
                .orElseThrow(MachineSetupException::new);

        Set<String> beverages = allBeverages.keySet();

        List<Beverage> beveragesList = new ArrayList<>(beverages.size());

        for (String beverage : beverages) {

            JsonObject beverageObject = Optional.of(allBeverages.getAsJsonObject(beverage)).orElseThrow(MachineSetupException::new);

            Recipe recipe = makeRecipeFromBeverageJsonObject(beverageObject);

            beveragesList.add(new Beverage(beverage, recipe));
        }

        return beveragesList;
    }

    private static Optional<Integer> fetchNoOfOutlets(JsonObject machineObject) throws MachineSetupException {
        return Optional.of(machineObject.getAsJsonObject("outlets")).map(outlet -> outlet.getAsJsonPrimitive("count_n"))
                .map(JsonPrimitive::getAsInt);
    }


    private static Recipe makeRecipeFromBeverageJsonObject(JsonObject beverage) {
        Set<String> ingredients = beverage.keySet();

        List<RecipeIngredient> recipeIngredients = new ArrayList<>(ingredients.size());

        for (String ingredient : ingredients) {
            Integer quantity = beverage.getAsJsonPrimitive(ingredient).getAsInt();
            recipeIngredients.add(new RecipeIngredient(ingredient, quantity));
        }

        return new Recipe(recipeIngredients);
    }


    //testing multiple orders simultaneously
    public static void main(String[] args) throws InterruptedException {

        String coffeeMachineSetupInfo = "{\n" +
                "    \"machine\": {\n" +
                "      \"outlets\": {\n" +
                "        \"count_n\": 4\n" +
                "      },\n" +
                "      \"total_items_quantity\": {\n" +
                "        \"hot_water\": 500,\n" +
                "        \"hot_milk\": 500,\n" +
                "        \"ginger_syrup\": 100,\n" +
                "        \"sugar_syrup\": 100,\n" +
                "        \"tea_leaves_syrup\": 100\n" +
                "      },\n" +
                "      \"beverages\": {\n" +
                "        \"hot_tea\": {\n" +
                "          \"hot_water\": 200,\n" +
                "          \"hot_milk\": 100,\n" +
                "          \"ginger_syrup\": 10,\n" +
                "          \"sugar_syrup\": 10,\n" +
                "          \"tea_leaves_syrup\": 30\n" +
                "        },\n" +
                "        \"hot_coffee\": {\n" +
                "          \"hot_water\": 100,\n" +
                "          \"ginger_syrup\": 30,\n" +
                "          \"hot_milk\": 400,\n" +
                "          \"sugar_syrup\": 50,\n" +
                "          \"tea_leaves_syrup\": 30\n" +
                "        },\n" +
                "        \"black_tea\": {\n" +
                "          \"hot_water\": 300,\n" +
                "          \"ginger_syrup\": 30,\n" +
                "          \"sugar_syrup\": 50,\n" +
                "          \"tea_leaves_syrup\": 30\n" +
                "        },\n" +
                "        \"green_tea\": {\n" +
                "          \"hot_water\": 100,\n" +
                "          \"ginger_syrup\": 30,\n" +
                "          \"sugar_syrup\": 50,\n" +
                "          \"green_mixture\": 30\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "}";
        CoffeeMachine coffeeMachine = setupCoffeeMachine(JsonParser.parseString(coffeeMachineSetupInfo).getAsJsonObject());

        Thread t1 = new Thread(() -> {
            try {
                System.out.println(coffeeMachine.orderBeverage("hot_tea"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                System.out.println(coffeeMachine.orderBeverage("hot_coffee"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");


        Thread t3 = new Thread(() -> {
            try {
                System.out.println(coffeeMachine.orderBeverage("hot_coffee"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3");

        Thread t4 = new Thread(() -> {
            try {
                System.out.println(coffeeMachine.orderBeverage("black_tea"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t4");

        Thread t5 = new Thread(() -> {
            try {
                System.out.println(coffeeMachine.orderBeverage("hot_tea"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");


        Thread t6 = new Thread(() -> {
            try {
                System.out.println(coffeeMachine.orderBeverage("Green Tea"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t6");


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
