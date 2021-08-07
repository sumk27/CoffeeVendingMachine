package com.sumeet.coffeeMachine;

import com.google.gson.JsonParser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static com.sumeet.coffeeMachine.CoffeeVendingMachineDriver.setupCoffeeMachine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Note all the testcases should run in their specific order
//As CoffeeMachine is initialized only once before all the test cases
public class FT_CoffeeMachine {

    private static CoffeeMachine coffeeMachine;

    @BeforeAll
    static void setup() {
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
         coffeeMachine = setupCoffeeMachine(JsonParser.parseString(coffeeMachineSetupInfo).getAsJsonObject());
    }

    @Test
    void testValidCases() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            try {
                assertEquals("**** hot_coffee is ready to be served. ****", coffeeMachine.orderBeverage("hot_coffee"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
    }

    @Test
    void testForLowIngredient() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            try {
                assertEquals("**** hot_tea is ready to be served. ****", coffeeMachine.orderBeverage("hot_tea"));
                assertTrue(coffeeMachine.getLowIndicatorsForAllIngredients().contains("hot_milk"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
    }


    @Test
    void testWrongOrder() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            try {
                assertEquals("---- Cannot server Beverage!!! We don't server latte !!! ----", coffeeMachine.orderBeverage("latte"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
    }


    @Test
    void testCannotServeDrinkDueToLowIngredient() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            try {
                assertEquals("---- Ingredient hot_milk is not available in sufficient amount for hot_coffee ----", coffeeMachine.orderBeverage("hot_coffee"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
    }


    @Test
    void testCannotServeDrinkDueToIngredientNotPresent() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            try {
                assertEquals("---- Ingredient green_mixture is not available for green_tea ----", coffeeMachine.orderBeverage("green_tea"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
    }




}
